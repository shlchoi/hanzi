package ca.uwaterloo.sh6choi.hanzi.utils;

import android.content.Context;

import java.util.HashMap;
import java.util.List;

import ca.uwaterloo.sh6choi.hanzi.model.ChineseNumber;
import ca.uwaterloo.sh6choi.hanzi.database.DatabaseRequestCallback;
import ca.uwaterloo.sh6choi.hanzi.database.NumberDataSource;

/**
 * Created by Samson on 2015-11-02.
 */
public class NumberUtils {
    private static HashMap<Integer, ChineseNumber> sChineseNumberMap;

    public static void refreshMap(Context context) {
        sChineseNumberMap = new HashMap<>();
        NumberDataSource dataSource = new NumberDataSource(context);
        dataSource.open();
        dataSource.queryNumbers(new DatabaseRequestCallback<List<ChineseNumber>>() {
            @Override
            public void processResults(List<ChineseNumber> results) {
                for (int i = 0; i < results.size(); i ++) {
                    sChineseNumberMap.put(results.get(i).getNumber(), results.get(i));
                }
            }
        });
    }

    public static String getTraditionalChineseNumber(int number) {
        StringBuilder builder = new StringBuilder();

        if (number / 100000000 > 0) {
            int multiple = number / 100000000;
            if (multiple > 1) {
                builder.append(getTraditionalChineseNumber(multiple));
            }
            builder.append(sChineseNumberMap.get(100000000).getTraditional());
            if (number % 100000000 > 0) {
                builder.append(getTraditionalChineseNumber(number % 100000000));
            }
            return builder.toString();
        }

        if (number / 10000 > 0) {
            int multiple = number / 10000;
            if (multiple > 1) {
                builder.append(getTraditionalChineseNumber(multiple));
            }
            builder.append(sChineseNumberMap.get(10000).getTraditional());
            if (number % 10000 > 0) {
                builder.append(getTraditionalChineseNumber(number % 10000));
            }
            return builder.toString();
        }

        if (number / 1000 > 0) {
            int multiple = number / 1000;
            if (multiple > 1) {
                builder.append(getTraditionalChineseNumber(multiple));
            }
            builder.append(sChineseNumberMap.get(1000).getTraditional());
            if (number % 1000 > 0) {
                builder.append(getTraditionalChineseNumber(number % 1000));
            }
            return builder.toString();
        }

        if (number / 100 > 0) {
            int multiple = number / 100;
            if (multiple > 1) {
                builder.append(getTraditionalChineseNumber(multiple));
            }
            builder.append(sChineseNumberMap.get(100).getTraditional());
            if (number % 100 > 0) {
                builder.append(getTraditionalChineseNumber(number % 100));
            }
            return builder.toString();
        }

        if (number / 10 > 0) {
            int multiple = number / 10;
            if (multiple > 1) {
                builder.append(getTraditionalChineseNumber(multiple));
            }
            builder.append(sChineseNumberMap.get(10).getTraditional());
            if (number % 10 > 0) {
                builder.append(getTraditionalChineseNumber(number % 10));
            }
            return builder.toString();
        }

        return sChineseNumberMap.get(number).getTraditional();
    }

    public static String getSimplifiedChineseNumber(int number) {
        StringBuilder builder = new StringBuilder();

        if (number / 100000000 > 0) {
            int multiple = number / 100000000;
            if (multiple > 1) {
                builder.append(getTraditionalChineseNumber(multiple));
            }
            builder.append(sChineseNumberMap.get(100000000).getSimplified());
            if (number % 100000000 > 0) {
                builder.append(getTraditionalChineseNumber(number % 100000000));
            }
            return builder.toString();
        }

        if (number / 10000 > 0) {
            int multiple = number / 10000;
            if (multiple > 1) {
                builder.append(getTraditionalChineseNumber(multiple));
            }
            builder.append(sChineseNumberMap.get(10000).getSimplified());
            if (number % 10000 > 0) {
                builder.append(getTraditionalChineseNumber(number % 10000));
            }
            return builder.toString();
        }

        if (number / 1000 > 0) {
            int multiple = number / 1000;
            if (multiple > 1) {
                builder.append(getTraditionalChineseNumber(multiple));
            }
            builder.append(sChineseNumberMap.get(1000).getSimplified());
            if (number % 1000 > 0) {
                builder.append(getTraditionalChineseNumber(number % 1000));
            }
            return builder.toString();
        }

        if (number / 100 > 0) {
            int multiple = number / 100;
            if (multiple > 1) {
                builder.append(getTraditionalChineseNumber(multiple));
            }
            builder.append(sChineseNumberMap.get(100).getSimplified());
            if (number % 100 > 0) {
                builder.append(getTraditionalChineseNumber(number % 100));
            }
            return builder.toString();
        }

        if (number / 10 > 0) {
            int multiple = number / 10;
            if (multiple > 1) {
                builder.append(getTraditionalChineseNumber(multiple));
            }
            builder.append(sChineseNumberMap.get(10).getSimplified());
            if (number % 10 > 0) {
                builder.append(getTraditionalChineseNumber(number % 10));
            }
            return builder.toString();
        }

        return sChineseNumberMap.get(number).getSimplified();
    }
}
