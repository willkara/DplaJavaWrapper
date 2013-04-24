# Java Wrapper for the Digital Public Library's API


Javadoc available at: http://rci.rutgers.edu/~willkara/apidocs/


This is meant to be a simple Java wrapper for the Digital Public Library's API 

http://dp.la

https://github.com/dpla/platform




## Examples
--------------------------------

Before anything is done you must set your apikey.


    SearchQuery sq = new SearchQuery("pizza",null);
        
    sq.setAPIKEY("your api key goes here");



Every SearchQuery will return an array full of DplaItems. You can then gain access to each items information from there.

You can execute a search with:

* Just a Search Query
* Search Query & SearchOptions
* Just SearchOptions

If you only want one, then just pass null for the other.

### Execute a simple search query WITHOUT any SearchOptions.

    SearchQuery sq = new SearchQuery("pizza",null);
        
        sq.setAPIKEY("your api key goes here");
        DplaItem[] result = sq.search();

        for (DplaItem dp : result) {
            System.out.println(dp.getSourceResource().getTitle());
        }

### Execute a search query with some SearchOptions


    
    SearchOptions so = new SearchOptions();

    so.description="history";
    SearchQuery sq = new SearchQuery("pizza",null);
        
        sq.setAPIKEY("your api key goes here");
        DplaItem[] result = sq.search();

        for (DplaItem dp : result) {
            System.out.println(dp.getSourceResource().getTitle());
        }




