package com.example.lulavillalobos.bakingapp.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.lulavillalobos.bakingapp.Database.IngredientListConverter;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

@Entity (tableName = "recipe")
public class Recipe implements Parcelable {
    @PrimaryKey
    @SerializedName("id")
    private Integer id;

    @SerializedName("name")
    private String name;

    @SerializedName("ingredients")
    private List<Ingredient> ingredients;

    @SerializedName("steps")
    private List<Step> steps;

    @SerializedName("servings")
    private Integer servings;

    @SerializedName("image")
    private String image;

    public Integer getId() { return id; }

    public String getName() { return name; }

    public List<Ingredient> getIngredients() { return ingredients; }

    public List<Step> getSteps() { return steps; }

    public Integer getServings() { return servings; }

    public String getImage() { return image; }

    public void setId(Integer id) { this.id = id; }

    public void setName(String name) { this.name = name; }

    public void setIngredients(List<Ingredient> ingredients) { this.ingredients = ingredients; }

    public void setSteps(List<Step> steps) { this.steps = steps; }

    public void setServings(Integer servings) { this.servings = servings; }

    public void setImage(String image) { this.image = image; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeList(this.ingredients);
        dest.writeList(this.steps);
        dest.writeInt(this.servings);
        dest.writeString(this.image);
    }

    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    private Recipe(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.ingredients = new ArrayList<Ingredient>();
        in.readList(this.ingredients, Ingredient.class.getClassLoader());
        this.steps = new ArrayList<Step>();
        in.readList(this.steps, Step.class.getClassLoader());
        this.servings = in.readInt();
        this.image = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Recipe(int id, String name, List<Ingredient> ingredients,
                  List<Step> steps, int servings, String image) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.image = image;
    }
}
