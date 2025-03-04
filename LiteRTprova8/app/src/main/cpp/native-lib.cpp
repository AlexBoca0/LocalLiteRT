#include <jni.h>
#include <string>
#include "tensorflow/lite/c/c_api.h"

extern "C" JNIEXPORT jfloat JNICALL
Java_com_example_litertprova8_ResultActivity_executeModel(
        JNIEnv* env,
        jobject /* this */,
        jstring path,
        jfloatArray data) {



    // Dichiarazione delle variabili di input e output
    float input[10];
    float output[1];

    jfloat* data_elements = env->GetFloatArrayElements(data, nullptr);
    if (data_elements == nullptr) {
        // Handle error: Could not get array elements
        printf("Errore nell'acquisizione degli elementi dell'array\n");
        return -1;
    }

    // 4. Copy the data from jfloatArray to the C++ array
    for (int i = 0; i < 10; ++i) {
        input[i] = data_elements[i];
    }

    // 5. Release the jfloatArray elements (very important!)
    env->ReleaseFloatArrayElements(data, data_elements, JNI_ABORT);

    // Carica il modello
    TfLiteModel* model = TfLiteModelCreateFromFile(env->GetStringUTFChars(path, nullptr));
    if (!model) {
        printf("Errore nel caricamento del modello\n");
        return -1;
    }

    // Crea le opzioni per l'interprete
    TfLiteInterpreterOptions* options = TfLiteInterpreterOptionsCreate();
    TfLiteInterpreterOptionsSetNumThreads(options, 1);

    // Crea l'interprete
    TfLiteInterpreter* interpreter = TfLiteInterpreterCreate(model, options);
    if (!interpreter) {
        printf("Errore nella creazione dell'interprete\n");
        TfLiteInterpreterOptionsDelete(options);
        TfLiteModelDelete(model);
        return -1;
    }

    // Alloca i tensori
    if (TfLiteInterpreterAllocateTensors(interpreter) != kTfLiteOk) {
        printf("Errore nell'allocazione dei tensori\n");
        TfLiteInterpreterDelete(interpreter);
        TfLiteInterpreterOptionsDelete(options);
        TfLiteModelDelete(model);
        return -1;
    }

    // Ottieni il tensore di input
    TfLiteTensor* input_tensor = TfLiteInterpreterGetInputTensor(interpreter, 0);
    if (!input_tensor) {
        printf("Errore nell'acquisizione del tensore di input\n");
        TfLiteInterpreterDelete(interpreter);
        TfLiteInterpreterOptionsDelete(options);
        TfLiteModelDelete(model);
        return -1;
    }

    // Copia i dati di input nel tensore
    if (TfLiteTensorCopyFromBuffer(input_tensor, input, sizeof(input)) != kTfLiteOk) {
        printf("Errore nella copia dei dati di input\n");
        TfLiteInterpreterDelete(interpreter);
        TfLiteInterpreterOptionsDelete(options);
        TfLiteModelDelete(model);
        return -1;
    }

    // Esegue il modello
    if (TfLiteInterpreterInvoke(interpreter) != kTfLiteOk) {
        printf("Errore durante l'inferenza\n");
        TfLiteInterpreterDelete(interpreter);
        TfLiteInterpreterOptionsDelete(options);
        TfLiteModelDelete(model);
        return -1;
    }

    // Ottieni il tensore di output
    const TfLiteTensor* output_tensor = TfLiteInterpreterGetOutputTensor(interpreter, 0);
    if (!output_tensor) {
        printf("Errore nell'acquisizione del tensore di output\n");
        TfLiteInterpreterDelete(interpreter);
        TfLiteInterpreterOptionsDelete(options);
        TfLiteModelDelete(model);
        return -1;
    }

    // Copia i dati di output
    if (TfLiteTensorCopyToBuffer(output_tensor, output, sizeof(output)) != kTfLiteOk) {
        printf("Errore nella copia dei dati di output\n");
        TfLiteInterpreterDelete(interpreter);
        TfLiteInterpreterOptionsDelete(options);
        TfLiteModelDelete(model);
        return -1;
    }


    return output[0];
}