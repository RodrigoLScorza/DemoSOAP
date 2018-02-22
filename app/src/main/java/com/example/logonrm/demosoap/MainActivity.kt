package com.example.logonrm.demosoap

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.PropertyInfo
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE

class MainActivity : AppCompatActivity() {

    private val url = "http://10.3.2.42:8080/CalculadoraWSService/CalculadoraWS?wsdl";
    private val nameSpace = "http://heiderlopes.com.br/";
    private val methodName = "calcular";
    private val soapAction = nameSpace + methodName;
    private val parametro1 = "num1";
    private val parametro2 = "num2";
    private val parametro3 = "op";

    //lateinit var botao: Button
    // lateinit var edt1: EditText
    // lateinit var edt2: EditText
    //lateinit var tvResultado: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        botao = findViewById(R.id.btSomar)
//        edt1 = findViewById(R.id.EdtNum1)
//        edt2 = findViewById(R.id.EdtNum2)
//        //tvResultado = findViewById(R.id.tvResultado)

        btCalcular.setOnClickListener({
            callWebService().execute(edtNum1.text.toString(), edtNum2.text.toString(), spOperacao.selectedItem.toString())
        })


    }

    inner class callWebService : AsyncTask<String, Void, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            btCalcular.isEnabled = false
        }

        override fun doInBackground(vararg params: String?): String {
            var result = ""
            val soapObject = SoapObject(nameSpace, methodName)

            val numberInfo = PropertyInfo()
            numberInfo.name = parametro1
            numberInfo.value = params[0]
            numberInfo.type = Integer::class.java

            soapObject.addProperty(numberInfo)

            val numberInfo2 = PropertyInfo()
            numberInfo2.name = parametro2
            numberInfo2.value = params[1]
            numberInfo2.type = Integer::class.java

            soapObject.addProperty(numberInfo2)


            val numberInfo3 = PropertyInfo()
            numberInfo3.name = parametro3
            numberInfo3.value = params[2]
            numberInfo3.type = String::class.java

            soapObject.addProperty(numberInfo3)

            val envelope = SoapSerializationEnvelope(SoapEnvelope.VER11)
            envelope.setOutputSoapObject(soapObject)

            val httpTransporteSE = HttpTransportSE(url)
            try {
                httpTransporteSE.call(soapAction, envelope)
                val soapPrimitive = envelope.response
                result = soapPrimitive.toString()

            } catch (e: Exception) {
                e.printStackTrace()
                Log.i("FDP", e.printStackTrace().toString())
            }

            return result

        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

           // Toast.makeText(this@MainActivity, result, Toast.LENGTH_LONG).show()

            tvResultado.text = result
            btCalcular.isEnabled = true

        }
    }
}
