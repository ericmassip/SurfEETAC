package com.example.juan.surfeetac;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class Previsiones extends Activity implements OnItemSelectedListener {

    private Spinner spComunidades, spSpots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previsiones);

        this.spComunidades = (Spinner) findViewById(R.id.sp_comunidad);
        this.spSpots = (Spinner) findViewById(R.id.sp_spot);

        loadSpinnerComunidades();

    }

    /**
     * Populate the Spinner.
     */
    private void loadSpinnerComunidades() {

        // Create an ArrayAdapter using the string array and a default spinner
        // layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.Comunidades, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        this.spComunidades.setAdapter(adapter);

        // This activity implements the AdapterView.OnItemSelectedListener
        this.spComunidades.setOnItemSelectedListener(this);
        this.spSpots.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos,
                               long id) {

        switch (parent.getId()) {
            case R.id.sp_comunidad:

                // Retrieves an array
                TypedArray arraySpots = getResources().obtainTypedArray(
                        R.array.array_comunidad_a_spots);
                CharSequence[] spots = arraySpots.getTextArray(pos);
                arraySpots.recycle();

                // Create an ArrayAdapter using the string array and a default
                // spinner layout
                ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
                        this, android.R.layout.simple_spinner_item,
                        android.R.id.text1, spots);

                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // Apply the adapter to the spinner
                this.spSpots.setAdapter(adapter);

                break;

            case R.id.sp_spot:

                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Callback method to be invoked when the selection disappears from this
        // view. The selection can disappear for instance when touch is
        // activated or when the adapter becomes empty.
    }

    /**
     * Shows the selected strings from spinners.
     *
     * @param v
     *            The view that was clicked.
     */
    public void Buscar(View v) {
        Toast.makeText(getApplicationContext(),getString(R.string.message, spSpots.getSelectedItem()
                        .toString(), spComunidades.getSelectedItem().toString()),Toast.LENGTH_LONG).show();

        String selec = spSpots.getSelectedItem().toString();

        //Segun el spot seleccionado en el segundo spinner cargamos una url u otra
        if (selec.equals("Almeria")) {
            String link = "http://surfmediterraneo.com/previsiones/almeria/";
            Intent intent = null;
            intent = new Intent(intent.ACTION_VIEW, Uri.parse(link));
            startActivity(intent);
        }

        if (selec.equals("Carboneras")) {
            String link = "http://surfmediterraneo.com/previsiones/carboneras/";
            Intent intent = null;
            intent = new Intent(intent.ACTION_VIEW, Uri.parse(link));
            startActivity(intent);
        }

        if (selec.equals("Barcelona")) {
            String link = "http://surfmediterraneo.com/previsiones/barcelona/";
            Intent intent = null;
            intent = new Intent(intent.ACTION_VIEW, Uri.parse(link));
            startActivity(intent);
        }

        if (selec.equals("Cap de Creus")) {
            String link = "http://surfmediterraneo.com/previsiones/med6/";
            Intent intent = null;
            intent = new Intent(intent.ACTION_VIEW, Uri.parse(link));
            startActivity(intent);
        }

        if (selec.equals("Tarragona")) {
            String link = "http://surfmediterraneo.com/previsiones/tarragona/";
            Intent intent = null;
            intent = new Intent(intent.ACTION_VIEW, Uri.parse(link));
            startActivity(intent);
        }

        if (selec.equals("Alicante")) {
            String link = "http://surfmediterraneo.com/previsiones/alicante/";
            Intent intent = null;
            intent = new Intent(intent.ACTION_VIEW, Uri.parse(link));
            startActivity(intent);
        }

        if (selec.equals("Castellon")) {
            String link = "http://surfmediterraneo.com/previsiones/castellon/";
            Intent intent = null;
            intent = new Intent(intent.ACTION_VIEW, Uri.parse(link));
            startActivity(intent);
        }

        if (selec.equals("Gandia")) {
            String link = "http://surfmediterraneo.com/previsiones/gandia/";
            Intent intent = null;
            intent = new Intent(intent.ACTION_VIEW, Uri.parse(link));
            startActivity(intent);
        }

        if (selec.equals("Torrevieja")) {
            String link = "http://surfmediterraneo.com/previsiones/torrevieja/";
            Intent intent = null;
            intent = new Intent(intent.ACTION_VIEW, Uri.parse(link));
            startActivity(intent);
        }

        if (selec.equals("Valencia")) {
            String link = "http://surfmediterraneo.com/previsiones/valencia/";
            Intent intent = null;
            intent = new Intent(intent.ACTION_VIEW, Uri.parse(link));
            startActivity(intent);
        }

        if (selec.equals("Vinaros")) {
            String link = "http://surfmediterraneo.com/previsiones/vinaroz/";
            Intent intent = null;
            intent = new Intent(intent.ACTION_VIEW, Uri.parse(link));
            startActivity(intent);
        }

        if (selec.equals("Alcudia")) {
            String link = "http://surfmediterraneo.com/previsiones/alcudia/";
            Intent intent = null;
            intent = new Intent(intent.ACTION_VIEW, Uri.parse(link));
            startActivity(intent);
        }

        if (selec.equals("Ibiza")) {
            String link = "http://surfmediterraneo.com/previsiones/ibiza/";
            Intent intent = null;
            intent = new Intent(intent.ACTION_VIEW, Uri.parse(link));
            startActivity(intent);
        }

        if (selec.equals("Mahon")) {
            String link = "http://surfmediterraneo.com/previsiones/mahon/";
            Intent intent = null;
            intent = new Intent(intent.ACTION_VIEW, Uri.parse(link));
            startActivity(intent);
        }

        if (selec.equals("Palma de Mallorca")) {
            String link = "http://surfmediterraneo.com/previsiones/palma/";
            Intent intent = null;
            intent = new Intent(intent.ACTION_VIEW, Uri.parse(link));
            startActivity(intent);
        }

        if (selec.equals("Cartagena")) {
            String link = "http://surfmediterraneo.com/previsiones/cartagena/";
            Intent intent = null;
            intent = new Intent(intent.ACTION_VIEW, Uri.parse(link));
            startActivity(intent);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_previsiones, menu);
        return true;
    }
}