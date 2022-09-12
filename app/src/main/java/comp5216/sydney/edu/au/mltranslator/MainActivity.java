package comp5216.sydney.edu.au.mltranslator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

//import android.util.Log;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.common.model.RemoteModelManager;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.TranslateRemoteModel;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    // variables to connect with user elements
    Spinner mSpinnerLanguages;
    // text box to input text in English
    EditText mInputText;
    // textview to show the translated text
    TextView mTranslatedText;

    // variable holding the langauge name that the input string should be translated to
    String mTransLang;
    // variable to hold the input text string in English
    String mInputString;

    Translator englishOtherTranslator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Link the input text and the output translation text view
        mInputText = (EditText) findViewById(R.id.input_text);
        mTranslatedText = (TextView) findViewById(R.id.tranaslation);

        // Populate the dropdown list
        mSpinnerLanguages = findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.languages, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        mSpinnerLanguages.setAdapter(adapter);
        // get the default language as the German
        mTransLang = mSpinnerLanguages.getSelectedItem().toString();

        // start listner function for the dropdown list (spinner) and input text
        startSpinnerListner();
    }

    public void startSpinnerListner() {
        mSpinnerLanguages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                // assign the currently seelcted language to the member variable
                mTransLang = mSpinnerLanguages.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }

    public String getTranaslationLang(String lang){
        String constTranslateLang = null;

        switch (lang) {
            case "Hindi":
                constTranslateLang = TranslateLanguage.HINDI;
                break;
            case "French":
                constTranslateLang = TranslateLanguage.FRENCH;
                break;
            case "Chinese":
                constTranslateLang = TranslateLanguage.CHINESE;
                break;
            default:
                constTranslateLang = TranslateLanguage.GERMAN;
        }
        return constTranslateLang;
    }

    public void translateText(View view) {

        //Step 1
        // get the Target language according to the drop down list selection.
        String constTranslateLang;
        constTranslateLang = getTranaslationLang(this.mTransLang);

        // Step 2
        // Create a Translator object, configuring it with the source and target languages
        TranslatorOptions options = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(constTranslateLang)
                .build();
        englishOtherTranslator = Translation.getClient(options);

        // Step 3
        // Make sure the required translation model has been downloaded to the device
        DownloadConditions conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();

        Toast toast=Toast.makeText(getApplicationContext(),"Translate clicked!!!",Toast.LENGTH_LONG);
        toast.setMargin(50,50);
        toast.show();

        englishOtherTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(
                        new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                // Model downloaded successfully. Okay to start translating.
                                //Step 4:
                                runTranslation();

                                Toast toast=Toast.makeText(getApplicationContext(),"Model downloaded successfully",Toast.LENGTH_LONG);
                                toast.setMargin(50,50);
                                toast.show();
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Model couldn’t be downloaded or other internal error.
                                Toast toast = Toast.makeText(getApplicationContext(), "Model downloaded Failed", Toast.LENGTH_LONG);
                                toast.setMargin(50, 50);
                                toast.show();

                            }
                        });

    }

    public void runTranslation(){
        mInputString = mInputText.getText().toString();
        Task<String> result = englishOtherTranslator.translate(mInputString)
                .addOnSuccessListener(
                        new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(String translatedText) {

                                mTranslatedText.setText(translatedText);

                                // Translation successful.
                                Toast toast = Toast.makeText(getApplicationContext(), "Translation successful", Toast.LENGTH_LONG);
                                toast.setMargin(50, 50);
                                toast.show();

                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Translation successful.
                                Toast toast = Toast.makeText(getApplicationContext(), "Translation unsuccessful", Toast.LENGTH_LONG);
                                toast.setMargin(50, 50);
                                toast.show();
                            }
                        });
    }

}
