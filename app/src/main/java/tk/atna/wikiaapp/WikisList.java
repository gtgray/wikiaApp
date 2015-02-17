package tk.atna.wikiaapp;


import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Model to keep a list of wikis
 */
public class WikisList {

    @SerializedName("items") List<Id> ids;
    int batches;
    int currentBatch;

    /**
     * Converts list of ids to simple int array
     */
    int[] listToArray() {
        if(ids == null)
            return null;

        int[] idz = new int[ids.size()];
        int index = 0;
        for(Id item : ids) {
            idz[index++] = item.id;
        }
        return idz;
    }

    /**
     * Model of shrinked wiki object
     */
    static class Id {

        int id;
    }

/*
{
    "items": [
        {
            "id":765031,
            "name":"Dying Light Wiki",
            "hub":"Gaming",
            "language":"en",
            "topic":null,
            "domain":"dyinglight.wikia.com"
        },
        ...
        {}
    ],
    "next":3,
    "total":250,
    "batches":84,
    "currentBatch":15
}
*/

}
