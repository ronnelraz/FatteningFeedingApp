package com.ronnelrazo.fatteningfeedingapp;

import android.view.View;

import com.google.android.material.button.MaterialButton;

public interface ItemClickListener {
    void onClick(View v, int pos, MaterialButton button);
}
