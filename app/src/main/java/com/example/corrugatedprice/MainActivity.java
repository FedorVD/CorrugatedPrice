package com.example.corrugatedprice;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.HashMap;

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
    TextView primeCostTextView;
    EditText markupTextView;
    TextView costTextView;
    String LOG_TAG = "SQLite";
    AppDatabase db;
    RawDao rawDao;
    MarcDao markDao;
    CostFundsDao costFundsDao;
    HashMap<String,Double> profileList = new HashMap<String, Double>(){{
        put("B", 1.379);
        put("C", 1.459);
        put("E", 1.272);
    }};

    Integer boxLength, boxWidth, boxHeight, blankLength, blankWidth, markup;
    Double blankSquare, profile, rawCost, directCost, covenantCosts, primeCost, cost;
    //String mark;

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
        primeCostTextView = findViewById(R.id.primeCost);
        markupTextView = findViewById(R.id.markup);
        costTextView = findViewById(R.id.cost);

        /*profileList.put("B", 1.379);
        profileList.put("C", 1.459);
        profileList.put("E", 1.272);*/

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "costsDb").allowMainThreadQueries().build();
        rawDao = db.rawDao();
        markDao = db.markDao();
        costFundsDao = db.costFundsDao();

        /*Long idRaw;
        ArrayList<Raw> rawList = new ArrayList<Raw>(){{
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
        }};
        for (Raw raw : rawList){
            idRaw = rawDao.insert(raw);
        }

        Long idMark;
        ArrayList<Mark> markList = new ArrayList<Mark>(){{
            add(new Mark("T-22", "K-112", "B-90", "K-112"));
            add(new Mark("T-23", "K-115", "B-90", "K-115"));
            add(new Mark("T-24", "K-115", "B-112", "K-125"));
            add(new Mark("T-25", "K-125", "B-135", "K-135"));
            add(new Mark("T-26", "K-170", "B-135", "K-170"));
            add(new Mark("T-27", "K-170", "B-140", "K-170"));
        }};
        for (Mark mark : markList){
            idMark = markDao.insert(mark);
        }

        Long idCostFunds;
        ArrayList<CostFunds> costFundsList = new ArrayList<CostFunds>(){{
            add(new CostFunds("directCosts", 6.021747));
            add(new CostFunds("covenantCosts", 2.846013));
            add(new CostFunds("pelletizingCosts", 0.103864));
            add(new CostFunds("printingCosts", 0.275353));
        }};
        for (CostFunds costFund : costFundsList){
            idCostFunds = costFundsDao.insert(costFund);
        }*/

        String[] marks = {"T-22","T-23", "T-24", "T-25", "T-26", "T-27"};
        ArrayAdapter<String> adapterMarks = new ArrayAdapter(this, android.R.layout.simple_spinner_item, marks);
        adapterMarks.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        markSpinner.setAdapter(adapterMarks);

        String[] profiles = {"B", "C", "E"};
        ArrayAdapter<String> adapterProfiles = new ArrayAdapter(this, android.R.layout.simple_spinner_item, profiles);
        adapterProfiles.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        profileSpinner.setAdapter(adapterProfiles);

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //проверка заполнения всех полей
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
                //расчет размеров заготовоки и ее площали
                blankLength = 2 * (boxLength + boxWidth) + 55;
                blankWidth = boxWidth + boxHeight + 7;
                blankSquare = ((double)(blankLength * blankWidth)) / 1000000;

                //получение из базы данных сырьевой композиции по марке гофроккартона
                Mark mark = markDao.getByName(markSpinner.getSelectedItem().toString());
                markup = Integer.parseInt(markupTextView.getText().toString());         //определили наценку
                profile = profileList.get(profileSpinner.getSelectedItem().toString()); //определи профиль гофрокартона
                //получение из базы данных слоев гофрокартона
                Raw boxFirstLay = rawDao.getByName(mark.firstLay);
                Raw boxSecondLay = rawDao.getByName(mark.secondLay);
                Raw boxThirdLay = rawDao.getByName(mark.thirdLay);
                //расчет сырьевой составляющей себестоимости
                rawCost = (boxFirstLay.cost
                        + boxSecondLay.cost * profile
                        + boxThirdLay.cost)
                        * blankSquare;
                //определение прямых и косвенных затрат
                directCost = costFundsDao.getByName("directCosts").cost * blankSquare;
                covenantCosts = costFundsDao.getByName("covenantCosts").cost;
                if (palletizingCheckBox.isChecked()) {
                    covenantCosts += costFundsDao.getByName("pelletizingCosts").cost;
                }
                if (printingCheckBox.isChecked()){
                    covenantCosts += costFundsDao.getByName("printingCosts").cost;
                }
                covenantCosts *= blankSquare;
                // расчет себестиомости
                primeCost = rawCost + directCost + covenantCosts;
                //расчет цены с учетом наценки
                cost = primeCost * (1 + ((double)markup) / 100);
                //вывод результатов
                blankLengthTextView.setText(blankLength.toString());
                blankWidthTextView.setText(blankWidth.toString());
                blankSquareTextView.setText(String.format("%.6f", blankSquare));
                rawCostTextView.setText(String.format("%.6f", rawCost));
                directCostsTextView.setText(String.format("%.6f", directCost));
                covenantCostsTextView.setText(String.format("%.6f", covenantCosts));
                primeCostTextView.setText(String.format("%.6f", primeCost));
                costTextView.setText(String.format("%.2f", cost));
            }
        });

    }


}