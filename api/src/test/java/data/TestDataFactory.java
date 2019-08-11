package data;

import data.builders.CustomCategory;
import data.builders.CustomPet;
import data.builders.CustomTag;

public class TestDataFactory {

    public static CustomCategory.CustomCategoryBuilder aDefaultCategory() { return CustomCategory.builder(); }
    public static CustomTag.CustomTagBuilder aDefaultTag() { return CustomTag.builder(); }
    public static CustomPet.CustomPetBuilder aDefaultPet() { return CustomPet.builder(); }

}
