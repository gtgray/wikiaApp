package tk.atna.wikiaapp;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Model to keep wiki object with needed attributes
 */
public class Wiki implements Parcelable {

    int id;
    String hub;
    String title;
    String url;
    Stats stats;
    String desc;
    String image;
    @SerializedName("wam_score") double wamScore;

    /**
     * Stuff to make serialization
     */
    public static final Parcelable.Creator<Wiki> CREATOR = new Parcelable.Creator<Wiki>() {

        public Wiki createFromParcel(Parcel in) {
            return new Wiki(in);
        }

        public Wiki[] newArray(int size) {
            return new Wiki[size];
        }
    };

    protected Wiki(Parcel parcel) {
        id = parcel.readInt();
        hub = parcel.readString();
        title = parcel.readString();
        url = parcel.readString();
        stats = parcel.readParcelable(Stats.class.getClassLoader());
        desc = parcel.readString();
        image = parcel.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(hub);
        dest.writeString(title);
        dest.writeString(url);
        dest.writeParcelable(stats, 0);
        dest.writeString(desc);
        dest.writeString(image);
    }

    @Override
    public int describeContents() {
        return 0;
    }


    /**
     * Model to keep statistics of wiki object
     */
    static class Stats implements Parcelable {

        int edits;
        int articles;
        int pages;
        int users;
        int activeUsers;
        int images;
        int videos;
        int admins;


        /**
         * Stuff to make serialization
         */
        public static final Parcelable.Creator<Stats> CREATOR = new Parcelable.Creator<Stats>() {

            public Stats createFromParcel(Parcel in) {
                return new Stats(in);
            }

            public Stats[] newArray(int size) {
                return new Stats[size];
            }
        };

        protected Stats(Parcel parcel) {
            edits = parcel.readInt();
            articles = parcel.readInt();
            pages = parcel.readInt();
            users = parcel.readInt();
            activeUsers = parcel.readInt();
            images = parcel.readInt();
            videos = parcel.readInt();
            admins = parcel.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(edits);
            dest.writeInt(articles);
            dest.writeInt(pages);
            dest.writeInt(users);
            dest.writeInt(activeUsers);
            dest.writeInt(images);
            dest.writeInt(videos);
            dest.writeInt(admins);
        }

        @Override
        public int describeContents() {
            return 0;
        }

    }


/*

// detailed
{
    "items":{
        "159":{
            "id":159,
            "wordmark":"http:\/\/images.wikia.com\/lotr\/images\/8\/89\/Wiki-wordmark.png",
            "title":"The One Wiki to Rule Them All",
            "url":"http:\/\/lotr.wikia.com\/",
            "stats":{"edits":160438,"articles":5634,"pages":30691,"users":24057092,"activeUsers":150,"images":6786,"videos":208,"admins":10},
            "topUsers":[456662,4828844,12782,78566,5703357,5210039,93770,3068419,1065801,1728211],
            "headline":"One Wiki to Rule Them All",
            "lang":"en",
            "flags":[],
            "desc":"One Wiki to Rule Them All is the definitive knowledge base for the Lord of the Rings books and films as well as anything related to or of J.R.R. Tolkien's fantasy universe of Middle-earth, including role-playing games, video games, and more.",
            "image":"http:\/\/vignette1.wikia.nocookie.net\/wikiaglobal\/images\/a\/aa\/Wikia-Visualization-Main%2Clotr.png\/revision\/latest?cb=20140530091625",
            "wam_score":"98.0367",
            "original_dimensions":{"width":"320","height":"320"}
        },
        "ID":{},
        ...
    }
}
*/
}
