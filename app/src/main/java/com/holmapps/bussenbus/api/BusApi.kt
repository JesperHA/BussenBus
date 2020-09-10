package com.holmapps.bussenbus.api

import androidx.lifecycle.LiveData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface BusApi {
    /**
     * @param requestTime hh:mm:ss
     */
    @GET("http://www.rejseplanen.dk/bin/query.exe/mny?look_minx=14274082&look_maxx=15580768&look_miny=54950366&look_maxy=55352136&tpl=trains2json3&look_productclass=992&look_json=yes&performLocating=1&look_nv=get_ageofreport%7Cyes%7Cget_rtmsgstatus%7Cyes%7Cget_rtfreitextmn%7Cyes%7Czugposmode%7C2%7Cinterval%7C30000%7Cintervalstep%7C2000%7Cget_nstop%7Cyes%7Cget_pstop%7Cyes%7Cget_stopevaids%7Cyes%7Ctplmode%7Ctrains2json3%7C&interval=30000&intervalstep=2000&")
    fun getBusLocations(@Query("look_requesttime") requestTime: String): LiveData<Response<BusResponse>>

}