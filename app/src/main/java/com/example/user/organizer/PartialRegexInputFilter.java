package com.example.user.organizer;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//------Класс-фильтр для ввода с регулярным выражением---------------
//http://horribileru.blogspot.com/2011/12/edittext.html
public class PartialRegexInputFilter implements InputFilter {

    private Pattern mPattern;

    public PartialRegexInputFilter(String pattern){
        mPattern = Pattern.compile(pattern);
    }

    @Override
    public CharSequence filter(CharSequence source,
                               int sourceStart, int sourceEnd,
                               Spanned destination, int destinationStart,
                               int destinationEnd)
    {
        String textToCheck = destination.subSequence(0, destinationStart).
                toString() + source.subSequence(sourceStart, sourceEnd) +
                destination.subSequence(
                        destinationEnd, destination.length()).toString();

        Matcher matcher = mPattern.matcher(textToCheck);

        // Entered text does not match the pattern
        if(!matcher.matches()){

            // It does not match partially too
            if(!matcher.hitEnd()){
                return "";
            }

        }

        return null;
    }

}
