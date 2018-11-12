package com.hpe.tokenizer.scanners.json;

import com.hpe.tokenizer.types.TokenListWithRemainingText;
import com.hpe.utils.Result;

public abstract class JsonScanner {
    public abstract Result<TokenListWithRemainingText> run(String text);
}
