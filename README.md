# The problem & the requirements
Fetch raw JSONs, parse and build specific counters as detailed at [https://github.com/vas-test/test1](https://github.com/vas-test/test1).

**Note** that some of the requirements are ambiguous *(what makes for a row? is it a raw string? one with some of the fields present but not others? all of fields present but some having invalid values? etc)*.

Based on these, the following assumptions and choices were made.

# The solution
TLDR; 
* Fetching the given JSON file gives a stream of lines. Once a JSON file has been processed, it will NOT be processed again.
* Tokenizing each line gives a list of JSON tokens (keys and values) or an error.
* Parsing each list of tokens validates missing fields and invalid values, and builds a set of counters.
* Displaying some metrics and KPIs based on these counters.

## Running the application
Assumes [gradle](https://gradle.org/) is installed.

<code>gradle bootRun</code>

OR

<code>gradle build</code>  
<code>java -jar build/libs/hpe-0.0.1-SNAPSHOT.jar</code> 

## Assumptions
* Only fully valid JSON lines are counted towards different metrics (all fields are present, all values are valid - except for the format of the origin' and destination's msisdn; see further info bellow). 
This is based on the original requirements that state that all of the attributes are mandatory and that some of the field values are set to certain values.
E.g. a CALL row with all of the fields/values present but with an invalid status_code for example would be ignored (though the fields with errors counter would match in this case). 

* The counter for the "Number of messages with blank content" measures MSG rows with empty message_content rather than empty JSONs i.e. "{}" 

* We have 2 counters for number of calls origin and number of destination grouped by country code.

* We have 2 counters for OK/KO calls to represent the relationship between them.

* The list of "Word occurrence ranking for the given words in message_content keyValue" is given in descending order (the most often word found first).

## Important blocks

### The REST LogsController
Exposes 3 endpoints to work with logs:
* /process-json/{date} - fetches the JSON file based on the given date, processes the file and returns a 200 OK on success and a 404 if the file cannot be find. 
* /metrics - returns the metrics associated with the latest JSON file processed (based on the latest saved endTimestamp)
* /kpis - returns the KPIs associated with all of the JSON processed so far

### The Tokenizer
The tokenizer transforms a raw JSON string into a list of tokens skipping any whitespace between the tokens (but none inside values).

**Note** that the tokenizer is not a trivial implementation of a split-by-comma-then-split-by-colon list of instructions. It uses scanners (see <code>com.hpe.tokenizer.scanners</code>) to fetch as much as it is needed out of the original string returning what is left as a result.

**Output and side effects:**
* It doesn't do any validation other that assuring that the JSON is well structured i.e. only valid JSON strings are tokenized.
* As a result of running the process, it either returns a proper token list for a well formed JSON string or an error with no previous tokens (though properly parsed until then) for invalid JSONs.  
E.g. it turns <code>{"message_type": "CALL","timestamp": 1517645740,...}</code> into <code>["{" "message_type" ":" "CALL" "," "timestamp" ":" 1517645740 "," ... "}"]</code>

**Assumptions**:
* No nested types
* No escaped strings i.e. no "Hello \"World!\""
* Only strings or integer (java long) numbers for values

### The Parser
The parser takes a list of tokens (the result of the tokenization of a raw JSON) and returns a list of key-value pairs while also validating that each field type has the proper value type i.e. string or integer (in the mathematical sense, whole number).

Each field value is parsed, validated and constructed to the right type (e.g. string constants).

**Assumptions**:
* As part of tokenizing the raw JSON, the resulting tokens list is balanced i.e. it starts with a "{" token and ends with a "}" and has a well-formed list of key values tokens (one "key" token, followed by a ":" token, followed by a "value" one) while the key-value pairs themselves are separated by the "," token.
* The previous Tokenizer makes sure of this.

### Storage and services
All of the counters are stored in-memory in a cache and are available for the entire duration of running the application. The cache is the single source of truth for the metrics and counters that will be retrieved later on.

#### The CacheService
The service is the single source of managing the cache used for retrieving, inserting and updating the counters.

#### The JsonService
The service takes a stream of json raw strings, evaluates each string and builds a list of structured key-value pairs. Based on these pairs, we'll process the list of counters that will be used later on for metrics and KPIs.

**Note** that there is a single synchronized section i.e. <code>markJsonToProcess</code> as there should be only one thread per file writing to the cache. Reading from the cache is not synchronized.

### Managing MSISDN and country codes
I use the phonenumbers from Google to work with msisdns as it is not (and shouldn't be) part of this homework.

As a result (and based on the ambiguous requirements and my limited knowledge on the subject), it makes a few assumptions in the process as it takes a simplistic view on the format of the numbers (e.g. it assumes the given codes are international hence prepending a "+" when dealing with origins and destinations).

More analysis of the lib and on the msisdn format is required before making it a bullet-proof solution.
 


