package com.marjo.giftyfactoryback.utils;

import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;

public class CheckUtility {
    
    public final static Consumer<List<String>> validateAtLeastOneNotBlank = elements -> elements.stream()
            .filter(StringUtils::isNotBlank).findAny()
            .orElseThrow(() -> new IllegalStateException("At least one params should be not blank"));
}
