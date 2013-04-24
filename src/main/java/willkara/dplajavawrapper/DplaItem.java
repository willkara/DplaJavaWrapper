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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**
 *
 * This class represents each individual DPLA Item. It allows access to
 * information regarding the item. It has the subclass SourceResource which
 * contains all of the information in the SourceResource JSONObject
 *
 * @author willkara
 *
 *
 */
public class DplaItem {

    JSONObject jsonParent;

    /**
     * Takes in the JSONobject representing the
     *
     * @param j The JSON object for the individual item
     */
    public DplaItem(JSONObject j) {


        jsonParent = j;


    }

    /**
     *
     * @return The URL to the item
     */
    public URL getItemURL() {
        String shownAt = jsonParent.getString("isShownAt");
        URL shownAtUrl = null;
        try {
            shownAtUrl = new URL(shownAt);
        } catch (MalformedURLException ex) {
            Logger.getLogger(DplaItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return shownAtUrl;

    }

    /**
     *
     * @return Returns the sourceResource object. This contains much of the
     * information about the item.
     */
    public SourceResource getSourceResource() {

        SourceResource sr = new SourceResource(jsonParent);

        return sr;


    }

    /**
     * Gets the id string of the item.
     * @return The objects dpla id.
     */
    public String getID() {
        return jsonParent.getString("id");
    }

    /**
     *
     * @return The data provider of the item.
     */
    public String getDataProvider() {

        try {
            return jsonParent.getString("dataProvider");
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Returns the original string representing this item's json value.
     *
     * @return A String containing the JSON value for the item
     */
    public String getJSONString() {
        return jsonParent.toString();
    }
    
    
    

    //------------------------------------------------------------------------//
    /**
     * The SourceResource JSONObject of the item. It contains some useful
     * meta-data about the item.
     *
     * @author willkara
     *
     *
     *
     */
    public static class SourceResource {

        public JSONObject sr;

        /**
         *
         * Take in the parent json object and find/create the sourceResource
         * object.
         *
         * @param j The parent json object
         *
         */
        public SourceResource(JSONObject j) {
            super();
            sr = j.getJSONObject("sourceResource");
        }

        /**
         *
         *
         * @return The title of the item.
         */
        public String getTitle() {
            try {
                return sr.getString("title");
            } catch (JSONException e) {
                return null;
            }
        }

        /**
         *
         * @return The description of the item.
         */
        public String getDescription() {
            try {
                return sr.getString("description");
            } catch (JSONException e) {
                return null;
            }
        }

        /**
         *
         * @return An array of strings containing the subjects of the item. Each
         * item of the array is a different subject
         */
        public String[] getSubjects() {
            try {
                JSONArray subArray = sr.getJSONArray("subject");
                String sub = "";
                for (int i = 0; i < subArray.size(); i++) {
                    JSONObject rec = subArray.getJSONObject(i);
                    sub += rec.getString("name").trim() + ",";
                    // ...
                }
                return sub.split(",");
            } catch (JSONException e) {
                return null;
            }
        }

        /**
         *
         * Not all items may have copyright information
         *
         * @return The copyright information for each item.
         */
        public String getCopyRights() {
            try {
                return sr.getString("rights");
            } catch (JSONException e) {
                return null;
            }
        }

        /**
         *
         * @return The name of the language that the item is written in.
         */
        public String getLanguageName() {
            try {
                return getLanguageInfo().getJSONObject(0).getString("name");
            } catch (JSONException e) {
                return null;
            }
        }

        /**
         *
         * @return The JSONArray containing the langauge info.
         */
        public JSONArray getLanguageInfo() {
            try {
                return sr.getJSONArray("language");
            } catch (JSONException e) {
                return null;
            }
        }

        /**
         *
         * @return The iso639_3 representaton of the langauge the item was
         * written in.
         */
        public String getISOLanguage() {
            try {
                return getLanguageInfo().getJSONObject(0).getString("iso639_3");
            } catch (JSONException e) {
                return null;
            }
        }

        /**
         *
         * @return The format of the item.
         */
        public String getFormat() {
            try {
                return sr.getString("format");
            } catch (JSONException e) {
                return null;
            }
        }

        /**
         *
         * @return The publisher of the item.
         */
        public String getPublisher() {
            try {
                return sr.getString("publisher");
            } catch (JSONException e) {
                return null;
            }
        }

        /**
         *
         * @return A String array containing the creators of the item.
         */
        public String[] getCreators() {
            try {
                Object[] creatorJSONarray = sr.getJSONArray("creator").toArray();
                String[] creatorArray = new String[creatorJSONarray.length];
                int i = 0;
                for (Object creator : creatorJSONarray) {
                    creatorArray[i] = creator.toString();
                    i++;
                }
                return creatorArray;
            } catch (JSONException e) {
                return null;
            }
        }

        /**
         *
         * @return The group/person/entity that is providing the item.
         */
        public String getProviderName() {
            try {
                return sr.getJSONArray("provider").toString();
            } catch (JSONException e) {
                return null;
            }
        }

        /**
         *
         * @return A String containing the collection ID of the collection containing the item.
         */
        public String getCollectionID() {
            try {
                return sr.getJSONObject("collection").getString("id");
            } catch (JSONException e) {
                return null;
            }
        }

        /**
         *
         * @return The name of the collection that the item is in.
         */
        public String getCollectionNAME() {
            try {
                return sr.getJSONObject("collection").getString("name");
            } catch (JSONException e) {
                return null;
            }
        }

        /**
         *
         * @return The title of the collection from which the item belongs to.
         *
         */
        public String getCollectionTITLE() {
            try {
                return sr.getJSONObject("collection").getString("title");
            } catch (JSONException e) {
                return null;
            }
        }
    }
}
