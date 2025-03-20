package com.example.testemercadolivre.core.util

import com.example.testemercadolivre.BuildConfig

const val API_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
const val URL_CODE = "https://auth.mercadolivre.com.br/authorization?response_type=code&client_id=${BuildConfig.CLIENT_ID}&redirect_uri=https://www.google.com/"


//shared preferences
const val TOKEN_CODE = "TOKENCODE"