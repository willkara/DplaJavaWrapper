/*
 * Original Author: Wiliam Karavites
 * wkaravites@gmail.com
 * 
 * You are free to use this software as you wish as long as you follow the license below
 * I just ask that you attirbute the contributions of myself and other developers.
 * 
 * 
 * The MIT License (MIT)
 *
 * Copyright (c) <2013> <William Karavites>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package willkara.dplajavawrapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.apache.commons.io.IOUtils;
import org.apache.commons.httpclient.util.URIUtil;

/**
 *
 * Construct a search query and search for items. Returns an array full of
 * DplaItem's which then allows you to get information from them.
 *
 * @author willkara
 */
public class SearchQuery {

    //Please put in your own api_key here.
    private static String apikey = "";
    //Any word you would like to search for. * can be used as a wildcard.
    private String searchQuery;
    SearchOptions so;

    /**
     *
     * Construct a query to be searched. One of the parameters is neccasary. If
     * you do not wish to use any SearchOptions then just pass null in its
     * place.
     *
     * @param qu The term you wish to search for.
     * @param s The SearchOptions object you wish to associate with the
     * SearchQuery. Can be null if you do not wish to use any.
     *
     */
    public SearchQuery(String qu, SearchOptions s) {
        searchQuery = qu;
        so = s;

    }

    /**
     * You must run this method with your api_key first before executing any
     * queries
     *
     * If you do not have an api_key yet please run this command: curl -v -XPOST
     * http://api.dp.la/v2/api_key/you@your_email.com
     *
     *
     * @param key Your api_key
     */
    public void setAPIKEY(String key) {
        apikey = key;
    }

    /**
     * Search for the items. You must have constructed a SearchQuery first.
     *
     * @return An array containing the DPLA Items returned from the search
     * query.
     * @throws IOException
     */
    public DplaItem[] search() throws IOException {
        String queryURL = "";
        String durl;

        //search using JUST a search query
        if (so == null && searchQuery != null) {

            queryURL = URIUtil.encodeQuery(String.format("http://api.dp.la/v2/items?q=%s&%s", searchQuery, "api_key=" + apikey));

        }
        //search using JUST sourceResource fields
        if (so != null && searchQuery == null) {
            queryURL = URIUtil.encodeQuery(String.format("http://api.dp.la/v2/items?%s%s", "api_key=" + apikey, so.formatSearchOptions()));
        }
        //search using a search query and sourceResource fields
        if (so != null && searchQuery != null) {

            queryURL = URIUtil.encodeQuery(String.format("http://api.dp.la/v2/items?q=%s&%s" + "%s", searchQuery, "api_key=" + apikey, so.formatSearchOptions()));
        }


        /*
         * Construct the URL and then open the result of that URL. It will return a JSON string to you and will be serialized into a JSONObject.
         * It splits the 'docs' array which contains the individual items and puts them into an array full of the items.
         */

        InputStream in = new URL(queryURL).openStream();

        try {
            durl = IOUtils.toString(in);
        } finally {
            IOUtils.closeQuietly(in);
        }


        JSONObject resultObject = (JSONObject) JSONSerializer.toJSON(durl);



        /*
         * Create the array of DplaItems.
         * I have it being split at the docs JSONArray.
         * It is possible that there is a better way.
         */
        JSONArray resultArray = resultObject.getJSONArray("docs");
        DplaItem[] itemArray = new DplaItem[resultArray.size()];

        for (int i = 0; i < resultArray.size(); i++) {
            JSONObject rec = resultArray.getJSONObject(i);
            DplaItem di = new DplaItem(rec);

            itemArray[i] = di;

        }

        return itemArray;

    }

    /**
     *
     * Inner-class that will put together all of the search options possible for
     * a query. Not neccasary but will help you to narrow down results.
     *
     */
    public static class SearchOptions {

        public String title;
        public String description;
        public String subject;
        public String creator;
        public String type;
        public String publisher;
        public String format;
        public String rights;
        public String contributor;
        //State,City spatial searches
        public String spatialString;
        //coordinate search
        public String coords;
        //set to the default size.
        public int page_size = 10;
        //Searching by dates
        public String dateString;

        public SearchOptions() {
            super();
        }

        /**
         *
         * Makes sure all of the search fields are correctly formatted. It takes
         * in all of the options and spits out the string to be added.
         *
         * @return A formatted String so that it can be added to the URL Query.
         */
        private String formatSearchOptions() {
            String options = "";
            if (title != null) {
                options += "&sourceResource.title=" + title;
            }
            if (description != null) {
                options += "&sourceResource.description=" + description;
            }
            if (subject != null) {
                options += "&sourceResource.subject=" + subject;
            }
            if (creator != null) {
                options += "&sourceResource.creator=" + creator;
            }
            if (type != null) {
                options += "&sourceResource.type=" + type;
            }
            if (publisher != null) {
                options += "&sourceResource.publisher=" + publisher;
            }
            if (format != null) {
                options += "&sourceResource.format=" + format;
            }
            if (rights != null) {
                options += "&sourceResource.rights=+" + rights;
            }
            if (contributor != null) {
                options += "&sourceResource.contributor=" + contributor;
            }
            if (spatialString != null) {
                options += spatialString;
            }
            if (coords != null) {
                options += coords;
            }
            if (page_size != 0) {

                if (page_size <= 0) {
                    page_size = 10;
                }

                if (page_size > 100) {
                    page_size = 99;
                }
                options += "&page_size=" + page_size;
            }

            return options.trim();
        }

        /**
         *
         * Need to figure out a better name for the something variable It can
         * either be before or after
         *
         * @param something Can either be before or after
         * @param year The year you wish to use
         */
        public void setDateSearchString(String something, int year) {
            if (something.equals("before")) {
                dateString = "&sourceResorce.date.before=" + year;
            }
            if (something.equals("after")) {
                dateString = "&sourceResource.date.after=" + year;
            }


        }

        /**
         *
         * Takes in information about two dates(YYYY-MM-DD) and then formats the
         * search string for searching for an object between those two dates.
         *
         * @param year1 The year of the first date
         * @param month1 The month of the first date
         * @param day1 The day of the first date
         * @param year2 The year of the second date
         * @param month2 The month of the second date
         * @param day2 The day of the second date
         */
        public void setDateInBetweenSearchString(int year1, int month1, int day1, int year2, int month2, int day2) {

            String date1 = year1 + "-" + month1 + "-" + day1;
            String date2 = year2 + "-" + month2 + "-" + day2;

            dateString = "&sourceResource.date.after=" + date1 + "&sourceREsource.date.before=" + date2;



        }

        /**
         * Search for a location specific to a type of location: City State
         * Region
         *
         *
         * @param locationInput The location you wish to search for
         * @param spatialType As of now, a City or State
         */
        public void setSpatialSpecificOptions(String locationInput, String spatialType) {

            spatialString = String.format("&sourceResource.spatial.%s=%s", spatialType, locationInput);

        }

        /**
         * General spatial search. It will search for a location in no specific
         * field. Your query is sinply part of the location
         *
         *
         * @param locationInput The location you wish to search for as a general
         * spatial search.
         */
        public void setGeneralSpatialOptions(String locationInput) {
            spatialString = "&sourceResource.spatial=" + locationInput;
        }

        /**
         *
         * Method to set the latitude and longitude and distance from those two
         * points as search options.
         *
         * @param lat The latitude you wish to use. Must be between 0-90
         * @param lon The longitude you wish to use. Must be between -180-180
         * @param distance The distance from the point. You NEED to put in
         * mi(miles) or km(milometers)
         */
        public void setSpatialCoordsOptions(long lat, long lon, String distance) {

            if (distance != null) {

                coords = String.format("&sourceResource.spatial.coordinates=%f,%f&sourceResource.spatial.distance=%s", lat, lon, distance);

            } else {

                //Search within 20 miles(default)
                coords = String.format("&sourceResource.spatial.coordinates=%f,%f", lat, lon);
            }

        }
    }
}
