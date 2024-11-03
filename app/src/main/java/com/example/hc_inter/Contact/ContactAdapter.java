package com.example.hc_inter.Contact;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.hc_inter.R;

import java.util.List;

public class ContactAdapter extends ArrayAdapter<Contact> {

    public ContactAdapter(Context context, List<Contact> contacts) {
        super(context, 0, contacts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Contact contact = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.contact_list, parent, false);
        }

        TextView nameTextView = convertView.findViewById(R.id.contactNameTextView);
        TextView phoneNumberTextView = convertView.findViewById(R.id.contactPhoneNumberTextView);

        nameTextView.setText(contact.getName());
        phoneNumberTextView.setText(contact.getPhoneNumber());

        return convertView;
    }
}