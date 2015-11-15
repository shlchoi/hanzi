package ca.uwaterloo.sh6choi.hanzi.database;

/**
 * Created by Samson on 2015-10-23.
 */
public interface DatabaseRequestCallback<T> {
    void processResults(T results);
}
