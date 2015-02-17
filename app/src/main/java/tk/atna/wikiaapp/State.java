package tk.atna.wikiaapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * State of app content
 */
public class State implements Parcelable {

    /**
     * Chosen hub
     */
    String hub;

    /**
     * Page number
     */
    int batch;

    /**
     * Number of remained pages
     */
    int togo;


    public State() {
        this(ServerApi.Hub.ALL);
    }

    public State(String hub) {
        this.hub = hub;
        this.batch = 1;
    }

    /**
     * Makes state updating. Remained pages are not being updated
     */
    public State update(State from) {

        if(from == null)
            return this;

        if(from.hub != null)
            this.hub = from.hub;

        if(from.batch != 0)
            this.batch = from.batch;

        return this;
    }

    /**
     * Stuff to make serialization
     */
    public static final Parcelable.Creator<State> CREATOR = new Parcelable.Creator<State>() {

        public State createFromParcel(Parcel in) {
            return new State(in);
        }

        public State[] newArray(int size) {
            return new State[size];
        }
    };

    protected State(Parcel parcel) {
        this.hub = parcel.readString();
        this.batch = parcel.readInt();
        this.togo = parcel.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(hub);
        dest.writeInt(batch);
        dest.writeInt(togo);
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
