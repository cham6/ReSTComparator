# CompareReSTResponses
API Response Comparator:
======================

Problem Statement:
We are given with two files (txt, for say) with ReST APIs separated by new line character.
Develop an API which will take the above two files as input and parses them for the http
request endpoints. Perform HTTP calls (GET, for say) on the URLs that are parsed from file1
and file2.

Each file contains (http/https) api’s separated by new line.

File1                              File2
https://reqres.in/api/users/3         https://reqres.in/api/users/2
https://reqres.in/api/users/1         https://reqres.in/api/users?page=3
https://reqres.in/api/users/2         https://reqres.in/api/users/2
https://reqres.in/api/users?page=2    https://reqres.in/api/users?page=2
…. And so on

Your code should read the (http/https) requests from File 1 and File 2, use any http client
library, and compare the response of

File 1 : line 1 & File 2 : line 1 
File 1 : line 2 & File 2 : line 2 
File 1 : line 3 & File 2 : line 3 
....and so on

Print the output when responses are compared 

https://reqres.in/api/users/3 not equals https://reqres.in/api/users/1 
https://reqres.in/api/users/2 equals https://reqres.in/api/users/2 
https://reqres.in/api/users?page=1 equals https://reqres.in/api/users?page=1 

Approach:
1) Create an Interface IComparator
public Boolean compare(String url1, String url2) ;
public List<LineIterator> getURLs();
2) Implement the above interface as follows:
public class RESTComparator implements IComparator
3) The RESTComparator class has getURLs and compare methods implemented.
4) As the requirement is that there could be millions of REST APIs, I have used
FileUtils.lineIterator for reading the files. This provides a stream of data
instead of loading all the file lines into memory. Thus, eliminating any
OutOfMemoryExceptions.
5) The compare method is implemented in such a way that none of the exceptions or
errors are missed.
6) Have created RunComparator class with ‘main’ method to demonstrate a way to
invoke the implemented APIs.
7) Created ‘firstFile.txt’ and ‘secondFile.txt’ with API information present with new line
separator.
8) Created TestRESTComparator to test the functionality of getURLs and
compare methods.
9) Have used ‘restito’ framework for stub servers. Restito is used to mock the server
responses, thus used for testing the behavior of ‘compare’ method.
10) Used ‘TestNG’ as basic unit test framework. 
