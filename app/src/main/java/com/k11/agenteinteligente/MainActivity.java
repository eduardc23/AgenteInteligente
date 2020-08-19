package com.k11.agenteinteligente;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import com.k11.agenteinteligente.databinding.ActivityMainBinding;
import com.k11.agenteinteligente.logica.aspiradora;
import com.k11.agenteinteligente.logica.cuarto;
import com.k11.agenteinteligente.logica.lugar;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String[] ubicacionAspiradora = {"Cuadrante A", "Cuadrante B"};
    private String[] suciedad = {"Cuadrante A", "Cuadrante B", "Cuadrante A y B", "Ningun cuadrante"};
    private String[] intentosSinSuciedad = {"0", "1", "2", "3", "4", "5", "6"};
    private lugar objLugar;
    private cuarto cuartoA;
    private cuarto cuartoB;
    private cuarto[] cuartos;
    private int intentSinSuciedad;
    private AccelerateDecelerateInterpolator accelerateDecelerateInterpolator;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.iniciar.setOnClickListener(this);
        binding.resetear.setOnClickListener(this);
        prepareInterpolator();
        crearCuartos();
        iniciarSpinners();
    }


    private void iniciarSpinners() {
        ArrayAdapter<String> adapterUbicacionAspiradora;
        adapterUbicacionAspiradora = new ArrayAdapter<>(binding.getRoot().getContext(),
                android.R.layout.simple_list_item_1, ubicacionAspiradora);
        adapterUbicacionAspiradora.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.ubbicacionAspiradora.setAdapter(adapterUbicacionAspiradora);

        binding.ubbicacionAspiradora.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ubicarAspiradora(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> adapterSuciedad;
        adapterSuciedad = new ArrayAdapter<>(binding.getRoot().getContext(),
                android.R.layout.simple_list_item_1, suciedad);
        adapterSuciedad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.suciedad.setAdapter(adapterSuciedad);

        binding.suciedad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ubicarsuciedad(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> adaptarIntentoSinSuciedad;
        adaptarIntentoSinSuciedad = new ArrayAdapter<>(binding.getRoot().getContext(),
                android.R.layout.simple_list_item_1, intentosSinSuciedad);
        adaptarIntentoSinSuciedad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.intentosSinSuciedad.setAdapter(adaptarIntentoSinSuciedad);

        binding.intentosSinSuciedad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                intentSinSuciedad = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void ubicarsuciedad(int position) {
        switch (position) {
            case 0:
                cuartoA.suciedad = true;
                cuartoB.suciedad = false;
                binding.basuraA.setVisibility(View.VISIBLE);
                binding.basuraB.setVisibility(View.INVISIBLE);
                break;
            case 1:
                cuartoB.suciedad = true;
                cuartoA.suciedad = false;
                binding.basuraA.setVisibility(View.INVISIBLE);
                binding.basuraB.setVisibility(View.VISIBLE);
                break;
            case 2:
                cuartoA.suciedad = true;
                cuartoB.suciedad = true;
                binding.basuraA.setVisibility(View.VISIBLE);
                binding.basuraB.setVisibility(View.VISIBLE);
                break;
            case 3:
                cuartoA.suciedad = false;
                cuartoB.suciedad = false;
                binding.basuraA.setVisibility(View.INVISIBLE);
                binding.basuraB.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private void crearCuartos() {
        cuartoA = new cuarto(false, false);
        cuartoB = new cuarto(false, false);
    }

    private void ubicarAspiradora(int position) {
        switch (position) {
            case 0:
                cuartoA.aspiradora = true;
                cuartoB.aspiradora = false;
                binding.aspiradoraA.setVisibility(View.VISIBLE);
                binding.aspiradoraB.setVisibility(View.INVISIBLE);
                break;
            case 1:
                cuartoA.aspiradora = false;
                cuartoB.aspiradora = true;
                binding.aspiradoraA.setVisibility(View.INVISIBLE);
                binding.aspiradoraB.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iniciar) {
            inicarSimulacion();
        }
        if (v.getId() == R.id.resetear) {
            resetearSpinners();
        }
    }

    private void resetearSpinners() {
        finish();
        startActivity(getIntent());
    }

    private void inicarSimulacion() {
        binding.suciedad.setEnabled(false);
        binding.intentosSinSuciedad.setEnabled(false);
        binding.ubbicacionAspiradora.setEnabled(false);
        cuartos = new cuarto[]{cuartoA, cuartoB};
        objLugar = new lugar(cuartos);
        aspiradora objArtefacto = new aspiradora();
        if (objLugar.getCuarto()[0].suciedad && !objLugar.getCuarto()[1].suciedad) {
            objArtefacto.aspirar(0, objLugar);
            if (!objLugar.getCuarto()[0].aspiradora) {
                objLugar.getCuarto()[0].aspiradora = true;
                objLugar.getCuarto()[1].aspiradora = false;
                actualizarUI();
            }
            actualizarBasura();
        }
        if (objLugar.getCuarto()[1].suciedad && !objLugar.getCuarto()[0].suciedad) {
            objArtefacto.aspirar(1, objLugar);
            if (!objLugar.getCuarto()[1].aspiradora) {
                objLugar.getCuarto()[0].aspiradora = false;
                objLugar.getCuarto()[1].aspiradora = true;
                actualizarUI();
            }
            actualizarBasura();
        }
        if (objLugar.getCuarto()[0].suciedad && objLugar.getCuarto()[1].suciedad) {
            if (objLugar.getCuarto()[0].aspiradora) {
                objLugar.getCuarto()[0].aspiradora = false;
                objLugar.getCuarto()[1].aspiradora = true;
            } else {
                objLugar.getCuarto()[0].aspiradora = true;
                objLugar.getCuarto()[1].aspiradora = false;
            }
            objArtefacto.aspirar(0, objLugar);
            objArtefacto.aspirar(1, objLugar);
            actualizarUI();
            actualizarBasura();
        }
        verificarSuciedad();
    }


    private void verificarSuciedad() {
        if (!objLugar.getCuarto()[0].suciedad && !objLugar.getCuarto()[1].suciedad) {
            if(intentSinSuciedad>0){
                intentSinSuciedad -=1;
                if (objLugar.getCuarto()[0].aspiradora) {
                    prepareObjectAnimator(accelerateDecelerateInterpolator, 0);
                } else {
                    prepareObjectAnimator(accelerateDecelerateInterpolator, 1);
                }
            }
        }
    }

    private void prepareInterpolator() {
        accelerateDecelerateInterpolator = new AccelerateDecelerateInterpolator();
    }

    private void prepareObjectAnimator(TimeInterpolator timeInterpolator, int lugarAspiradora) {
        //float w = (float)playGround.getWidth();
        float propertyStart = 0f;
        String propertyName = "translationX";
        ObjectAnimator objectAnimator;
        if (lugarAspiradora == 0) {
            objectAnimator = ObjectAnimator.ofFloat(binding.aspiradoraA, propertyName, propertyStart, 500f);
        } else {
            objectAnimator = ObjectAnimator.ofFloat(binding.aspiradoraB, propertyName, propertyStart, -500f);
        }
        objectAnimator.setDuration(1000);
        objectAnimator.setRepeatCount(intentSinSuciedad);
        objectAnimator.setRepeatMode(ObjectAnimator.REVERSE);
        objectAnimator.setInterpolator(timeInterpolator);
        objectAnimator.start();
    }

    private void actualizarUI() {
        if (cuartos[0].aspiradora) {
            prepareObjectAnimator(accelerateDecelerateInterpolator, 1);
        }
        if (cuartos[1].aspiradora) {
            prepareObjectAnimator(accelerateDecelerateInterpolator, 0);
        }

    }

    public void actualizarBasura(){
        if (cuartos[0].suciedad) {
            binding.basuraA.setVisibility(View.VISIBLE);
        }
        if (cuartos[1].suciedad) {
            binding.basuraB.setVisibility(View.VISIBLE);
        }
        if (!cuartos[0].suciedad) {
            binding.basuraA.setVisibility(View.INVISIBLE);
        }
        if (!cuartos[1].suciedad) {
            binding.basuraB.setVisibility(View.INVISIBLE);
        }
    }
}