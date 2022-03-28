# CSV Joiner
Csv joiner is a simple scala programme which is supposed to merge two given csv files 
that share one column into one file.

Used libraries: os-lib (for file operations) and scalatest (for tests).

Programme arguments: 0 - first file name, 1 - second file name, 2 - joining column name, 3 - joining type (inner/right/left)

**Resources**

The application operates on folder places in catalogue `src/main/resources`

**Code**

The code is based on two Scala objects and one Scala class.

*CSVJoiner*

This is the object which contains the main function. The function is supposed to convert the programme arguments into string values and is responsible for processing exceptions that may occur in future operations.

*CSVJoinerConf*

This is the object which contains all configuration data. I decided to place here all the special string values. In my opinion, it makes the code more consistent and readable.

*FileJoiner*

This is the class that makes all the necessary operations.

Function `mergeFiles`

This is the function, which is supposed to decide what type of joining is going to be done.
It is done based on the `joinType` argument's value and marker by the `outer` parameter.
As the right-outer-join operation is a mirror version of the left-outer-join I decided to invoke the first operation
exactly the same as the second one but with switched files. If the user provided incorrect joinType/none joinType the
programme would throw an IllegalArgumentException. I think it is better to be sure what type of operation is needed to be done to avoid mistakes.

Function `getJoinColumnIndex`

The function is supposed to return the indexes of the joining column in both files.
If a column with a specified name does not occur in one or both files the programme returns nothing, because the joining operation cannot be done.

Function `processTheFirstFile`

If the joining operation can be done (both of the files have a provided joining column),
the function processes each line of the first file separately (file data are treated as a stream).
CSV file rows are split by the delimiter and joined with the rows from the second file in the `findSecondFileMatchingRows` function (the operation is O(n * m), where n - first file size, m - second file size).

Function `findSecondFileMatchingRows`

If the row from the second file shares a value in the joining column both rows are merged and written into the result file in the `mergeRows` function.
To perform an outer-joining operation, a dedicated boolean marks if the first file row has found its matching second file row (if not a certain first file row is written into the result file with empty in the second file rows fields).

Function `mergeRows`

The function merges two rows into one and writes it to the result file (the joining column from the second row is eliminated as it is redundant). 

Function `addLeftRightJoinRow`

The function adds values from the first row into the result file and adds empty spaces in the second-row spaces. It makes the result file correct.

**Tests**

*FileJoinerTest*
