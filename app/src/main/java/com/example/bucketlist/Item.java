package com.example.bucketlist;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "item")
public class Item implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private Long id;
    @ColumnInfo(name = "itemTitle")
    private String Title;
    @ColumnInfo(name = "itemDescription")
    private String Description;
    @ColumnInfo(name = "done")
    private boolean completed;


    public Item(String title, String description, boolean completed) {
        Title = title;
        Description = description;
        this.completed = completed;
    }

    public Item(Long id) {
        this.id = id;
    }

    protected Item(Parcel in) {
        Title = in.readString();
        Description = in.readString();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //dest.writeLong(id);
        dest.writeString(this.Title);
        dest.writeString(this.Description);
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isCompleted() {
        return completed;
    }


}
