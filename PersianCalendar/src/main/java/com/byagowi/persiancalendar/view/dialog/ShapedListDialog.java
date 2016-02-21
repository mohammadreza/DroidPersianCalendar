package com.byagowi.persiancalendar.view.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceDialogFragmentCompat;

import com.byagowi.persiancalendar.R;
import com.byagowi.persiancalendar.adapter.ShapedArrayAdapter;

import java.util.Arrays;

/**
 * Created by ebraminio on 2/21/16.
 */
public class ShapedListDialog extends PreferenceDialogFragmentCompat {

    public static ShapedListDialog newInstance(Preference preference) {
        ShapedListDialog fragment = new ShapedListDialog();
        Bundle bundle = new Bundle(1);
        bundle.putString("key", preference.getKey());
        fragment.setArguments(bundle);

        return fragment;
    }

    CharSequence selected;
    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);
        final ShapedListPreference listPref = (ShapedListPreference)getPreference();

        final CharSequence[] entriesValues = listPref.getEntryValues();

        ShapedArrayAdapter entriesAdapter = new ShapedArrayAdapter(getContext(),
                R.layout.select_dialog_singlechoice_material,
                Arrays.asList(listPref.getEntries()));

        selected = listPref.getSelected();
        int index = Arrays.asList(entriesValues).indexOf(selected);
        builder.setSingleChoiceItems(entriesAdapter, index, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listPref.setSelected(entriesValues[which].toString());
                getDialog().dismiss();
            }
        });

        builder.setPositiveButton("", null);
    }

    @Override
    public void onDialogClosed(boolean b) { }
}