package edu.jls6595.assignment2;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    String bodyText;
    String spinnerChoice;
    String runningTime = "Average Case";
    boolean getMin = false, insert = false, search = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        final FloatingActionButton fab = findViewById(R.id.email_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                buttonClicked(fab);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void operationsChecked(View view) {
        boolean checked = ((CheckBox)view).isChecked();

        switch(view.getId()) {
            case R.id.getMin_check:
                if(checked)
                    getMin = true;
                else
                    getMin = false;
                break;
            case R.id.insert_check:
                if(checked)
                    insert = true;
                else
                    insert = false;
                break;
            case R.id.search_check:
                if(checked)
                    search = true;
                else
                    search = false;
                break;
            default:
                Log.wtf("a checkbox that doesn't exist was checked or unchecked", "how did it even get into this default case???");
                break;
        }
    }

    public void runningTimeSelected(View view) {
        switch(view.getId()) {
            case R.id.avgCase_radio:
                runningTime = "Average Case";
                break;
            case R.id.worstCase_radio:
                runningTime = "Worst Case";
                break;
            default:
                Log.wtf("no radio button selected", "WTF there's no radio button selected");
                break;
        }
    }

    public void buttonClicked(FloatingActionButton view) {
        // TextViews to be filled with information
        TextView to_result = findViewById(R.id.to_result);
        TextView subject_result = findViewById(R.id.subject_result);
        TextView body_result = findViewById(R.id.email_body);

        // User input views
        TextView email_addr = findViewById(R.id.email_address);
        TextView email_subject = findViewById(R.id.email_subject);
        Spinner spinner = findViewById(R.id.spinner);

        // Reset To and Subject fields
        to_result.setText(R.string.to_result_base);
        subject_result.setText(R.string.subject_result_base);

        // Set email address and email subject line
        to_result.setText(to_result.getText().toString().concat(" " + email_addr.getText().toString()));
        subject_result.setText(subject_result.getText().toString().concat(" " + email_subject.getText().toString()));

        spinnerChoice = spinner.getSelectedItem().toString();
        Log.d("spinnerChoice", "spinnerChoice = " + spinnerChoice);
        Log.d("spinner compare", "spinnerChoice.compareTo(none) = " + spinnerChoice.compareTo("none"));

        // If no choice is selected in the spinner, clear the body text
        if(spinnerChoice.compareTo("none") == 0) {
            Log.d("spinnerChoice = (none)?", "spinnerChoice2 = " + spinnerChoice);
            bodyText = "";
            body_result.setText(bodyText);

            return;
        }

        // Start constructing body text string
        bodyText = "\n" + runningTime + " " + getText(R.string.middle) + " " + spinnerChoice + " " + getText(R.string.ops);

        Log.d("runningTime", "runningTime = " + runningTime);
        Log.d("bool getMin", "getMin = " + getMin);

        switch(spinnerChoice) {
            case "2-3 Tree":
                Log.d("2-3 Tree case", "hit 2-3 Tree case for body text");
                if(getMin)
                    bodyText = bodyText + getText(R.string.get_minimum) + " " + getText(R.string.btree) + "\n";
                if(insert)
                    bodyText = bodyText + getText(R.string.insert_check) + ": " + getText(R.string.btree) + "\n";
                if(search)
                    bodyText = bodyText + getText(R.string.search_check) + ": " + getText(R.string.btree);
                break;
            case "Binary Search Tree":
                if(runningTime == "Average Case") {
                    if (getMin)
                        bodyText = bodyText + getText(R.string.get_minimum) + " " + getText(R.string.bst_avg) + "\n";
                    if (insert)
                        bodyText = bodyText + getText(R.string.insert_check) + ": " + getText(R.string.bst_avg) + "\n";
                    if (search)
                        bodyText = bodyText + getText(R.string.search_check) + ": " + getText(R.string.bst_avg);
                }
                else { // runningTime == "Worst Case"
                    if(getMin)
                        bodyText = bodyText + getText(R.string.get_minimum) + " " + getText(R.string.bst_worst) + "\n";
                    if(insert)
                        bodyText = bodyText + getText(R.string.insert_check) + ": " + getText(R.string.bst_worst) + "\n";
                    if(search)
                        bodyText = bodyText + getText(R.string.search_check) + ": " + getText(R.string.bst_worst);
                }
                break;
            case "Hash Table":
                switch(runningTime) {
                    case "Average Case":
                        if(getMin)
                            bodyText = bodyText + getText(R.string.get_minimum) + " " + getText(R.string.hash_avg) + "\n";
                        if(insert)
                            bodyText = bodyText + getText(R.string.insert_check) + ": " + getText(R.string.hash_avg) + "\n";
                        if(search)
                            bodyText = bodyText + getText(R.string.search_check) + ": " + getText(R.string.hash_avg);
                        break;
                    case "Worst Case":
                        if(getMin)
                            bodyText = bodyText + getText(R.string.get_minimum) + " " + getText(R.string.hash_worst) + "\n";
                        if(insert)
                            bodyText = bodyText + getText(R.string.insert_check) + ": " + getText(R.string.hash_worst) + "\n";
                        if(search)
                            bodyText = bodyText + getText(R.string.search_check) + ": " + getText(R.string.hash_worst);
                        break;
                }
                break;
            case "Linked List":
                if(getMin)
                    bodyText = bodyText + getText(R.string.get_minimum) + " " + getText(R.string.linkedList_minsearch) + "\n";
                if(insert)
                    bodyText = bodyText + getText(R.string.insert_check) + " " + getText(R.string.linkedList_insert) + "\n";
                if(search)
                    bodyText = bodyText + getText(R.string.search_check) + ": " + getText(R.string.linkedList_minsearch);
                break;
            case "Min Heap":
                if(getMin)
                    bodyText = bodyText + getText(R.string.get_minimum) + " " + getText(R.string.minHeap_getMin) + "\n";
                if(insert)
                    bodyText = bodyText + getText(R.string.insert_check) + ": " + getText(R.string.minHeap_insertsearch) + "\n";
                if(search)
                    bodyText = bodyText + getText(R.string.search_check) + ": " + getText(R.string.minHeap_insertsearch);
                break;
            default:
                Log.d("default in switch", "hit default case in switch for rest of body text");
                break;
        }

        // Set the text in the view
        body_result.setText(bodyText);

        Log.d("body result", body_result.getText().toString());
        return;
    }
}
