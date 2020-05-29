package com.example.assignment;


public class CustomDataModel {
    private String labelName, color_id;
    private boolean isSelected;


    public CustomDataModel(String color_id,String labelName, boolean isSelected) {
        this.labelName = labelName;
        this.isSelected = isSelected;
        this.color_id = color_id;
    }



    public String getColor_id() {
        return color_id;
    }

    public void setColor_id(String color_id) {
        this.color_id = color_id;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

}