package com.example.corrugatedprice;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Spinner markSpinner;
    Spinner profileSpinner;
    EditText boxLengthEditText;
    EditText boxWidthEditText;
    EditText boxHeightEditText;
    TextView blankLengthTextView;
    TextView blankWidthTextView;
    TextView blankSquareTextView;
    CheckBox palletizingCheckBox;
    CheckBox printingCheckBox;
    Button calculateButton;
    TextView rawCostTextView;
    TextView directCostsTextView;
    TextView covenantCostsTextView;
    EditText markupTextView;
    TextView costTextView;
    String LOG_TAG = "SQLite";
    AppDatabase db;
    RawDao rawDao;
    MarcDao markDao;
    CostFundsDao costFundsDao;
    HashMap<String,Double> profileList = new HashMap<String, Double>();

    Integer boxLength, boxWidth, boxHeight, blankLength, blankWidth, markup;
    Double blankSquare, profile, rawCost, directCost, covenantCosts, cost;
    String mark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        markSpinner = findViewById(R.id.cardMark);
        profileSpinner = findViewById(R.id.cardProfile);
        boxLengthEditText = findViewById(R.id.boxLength);
        boxWidthEditText = findViewById(R.id.boxWidth);
        boxHeightEditText = findViewById(R.id.boxHeight);
        blankLengthTextView = findViewById(R.id.blankLength);
        blankWidthTextView = findViewById(R.id.blankWidth);
        blankSquareTextView =findViewById(R.id.blankSquare);
        palletizingCheckBox = findViewById(R.id.isPallet);
        printingCheckBox = findViewById(R.id.isPrint);
        calculateButton = findViewById(R.id.calculate);
        rawCostTextView = findViewById(R.id.rawCost);
        directCostsTextView = findViewById(R.id.directCost);
        covenantCostsTextView = findViewById(R.id.covenantCost);
        markupTextView = findViewById(R.id.markup);
        costTextView = findViewById(R.id.cost);

        profileList.put("B", 1.379);
        profileList.put("C", 1.459);
        profileList.put("E", 1.272);

        /*db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "costsDb").build();
        rawDao = db.rawDao();
        markDao = db.markDao();
        costFundsDao = db.costFundsDao();*/

        Long id;
        /*ArrayList<Raw> rawList = new ArrayList<Raw>(){{
            add(new Raw("K-112", 5.6));
            add(new Raw("B-90", 4.05));
            add(new Raw("K-115", 5.75));
            add(new Raw("K-155", 7.75));
            add(new Raw("B-112", 5.04));
            add(new Raw("K-125", 6.25));
            add(new Raw("B-135", 5.6));
            add(new Raw("K-135", 6.75));
            add(new Raw("K-170", 8.5));
            add(new Raw("B-140", 6.3));
        }};*/
        /*for (Raw raw : rawList){
            id = rawDao.insert(raw);
        }
        ArrayList<Mark> markList = new ArrayList<Mark>(){{
            add(new Mark("T-22", "K-112", "B-90", "K-112"));
            add(new Mark("T-23", "K-115", "B-90", "K-115"));
            add(new Mark("T-24", "K-115", "B-112", "K-125"));
            add(new Mark("T-25", "K-125", "B-135", "K-135"));
            add(new Mark("T-26", "K-170", "B-135", "K-170"));
            add(new Mark("T-27", "K-170", "B-140", "K-170"));
        }};

        ArrayList<CostFunds> coastFundsList = new ArrayList<CostFunds>(){{
            add(new CostFunds("directCosts", 6.021747));
            add(new CostFunds("covenantCosts", 2.846013));
            add(new CostFunds("pelletizingCosts", 0.103864));
            add(new CostFunds("printingCosts", 0.275353));
        }};
        */

        Map<String, Double> rawList = new HashMap<String, Double>(){{
            put("K-112", 5.6);
            put("B-90", 4.05);
            put("K-115", 5.75);
            put("K-155", 7.75);
            put("B-112", 5.04);
            put("K-125", 6.25);
            put("B-135", 5.6);
            put("K-135", 6.75);
            put("K-170", 8.5);
            put("B-140", 6.3);
        }};

        Map<String,MarkStamp> MarkList = new HashMap<String, MarkStamp>(){{
            put("T-22", new MarkStamp("K-112", "B-90", "K-112"));
            put("T-23", new MarkStamp("K-115", "B-90", "K-115"));
            put("T-24", new MarkStamp("K-115", "B-112", "K-125"));
            put("T-25", new MarkStamp("K-125", "B-135", "K-135"));
            put("T-26", new MarkStamp("K-170", "B-135", "K-170"));
            put("T-27", new MarkStamp("K-170", "B-140", "K-170"));
        }};

        Map<String,Double> costFudsList = new HashMap<String, Double>(){{
           put("directCosts", 6.021747);
           put("covenantCosts", 2.846013);
           put("pelletizingCosts", 0.103864);
           put("printingCosts", 0.275353);
        }};

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (markSpinner.getSelectedItem().toString().isEmpty() ||
                        profileSpinner.getSelectedItem().toString().isEmpty() ||
                        boxLengthEditText.getText().toString().isEmpty()||
                        boxWidthEditText.getText().toString().isEmpty() ||
                        boxHeightEditText.getText().toString().isEmpty() ||
                        markupTextView.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Заполните параметры расчета цены гофротары", Toast.LENGTH_SHORT).show();
                    return;
                }
                boxLength = Integer.parseInt(boxLengthEditText.getText().toString());
                boxWidth = Integer.parseInt(boxWidthEditText.getText().toString());
                boxHeight = Integer.parseInt(boxHeightEditText.getText().toString());
                blankLength = 2 * (boxLength + boxWidth) + 55;
                blankWidth = boxWidth + boxHeight + 7;
                blankSquare = ((double)(blankLength * blankWidth)) / 1000000; //площадь заготовки ящика
                mark = markSpinner.getSelectedItem().toString();
                markup = Integer.parseInt(markupTextView.getText().toString());
                profile = profileList.get(profileSpinner.getSelectedItem().toString());
                rawCost = (rawList.get(MarkList.get(mark).firstLay)
                        + rawList.get(MarkList.get(mark).secondLay) * profile
                        + rawList.get(MarkList.get(mark).thirdLay)) * blankSquare;
                directCost = costFudsList.get("directCosts");
                covenantCosts = costFudsList.get("covenantCosts");
                if (palletizingCheckBox.isChecked()) {
                    covenantCosts += costFudsList.get("pelletizingCosts");
                }
                if (printingCheckBox.isChecked()){
                    covenantCosts += costFudsList.get("printingCosts");
                }
                covenantCosts *= blankSquare;
                cost = (rawCost + directCost + covenantCosts) * (1 + ((double)markup) / 100);
                blankLengthTextView.setText(blankLength.toString());
                blankWidthTextView.setText(blankWidth.toString());
                blankSquareTextView.setText(String.format("%.6f", blankSquare));
                rawCostTextView.setText(String.format("%.6f", rawCost));
                directCostsTextView.setText(String.format("%.6f", directCost));
                covenantCostsTextView.setText(String.format("%.6f", covenantCosts));
                costTextView.setText(String.format("%.6f", cost));
            }
        });

    }


}