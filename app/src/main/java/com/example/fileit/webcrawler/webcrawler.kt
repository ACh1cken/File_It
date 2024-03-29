package com.example.fileit.webcrawler

//import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.skrape.core.htmlDocument
import it.skrape.fetcher.*
import it.skrape.selects.html5.*
import kotlinx.coroutines.launch

data class ExtractedData (
    var announcement: String?,
    var announcementLink: String?,
    var announcementDate: String?,

    )

data class ExtractedDataDetails(
    var announcementTitle: String?,
    var announcementDetails: String?,
    var announcementDetailLinks: Map<String?,String?>,
)

class webcrawler : ViewModel() {
    private var _extractedData: MutableLiveData<List<ExtractedData>> = MutableLiveData(emptyList())
    var extractedData: LiveData<List<ExtractedData>> = _extractedData
    var _initCount : Int = 0

    init {
//        Log.e("ViewModel","Created")
        }

    fun updateInit(){
        _initCount++
    }

    fun updateData() {
        viewModelScope.launch {

            _extractedData.postValue(fetch())
            println(extractedData)
            extractedData = _extractedData
//            Log.e("Extracted","$extractedData")
        }
    }

    fun fetchData():LiveData<List<ExtractedData>>{
        return this.extractedData
    }

    fun flush() {
        _extractedData.postValue(emptyList())
    }


 fun fetch(): List<ExtractedData> {
        val extracted = skrape(HttpFetcher) {
                request {
                    method = Method.GET
//                    Log.e("method","$method")
                    followRedirects = false
                    //access cached copy because cant bypass anti crawler protection
                    url = "https://webcache.googleusercontent.com/search?q=cache:https://www.hasil.gov.my/en/announcement/"
                    //url = "https://www.hasil.gov.my/en/announcement/"
                    timeout =15000
                    userAgent="Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.97 Safari/537.36"
                }
            response(fun Result.(): List<ExtractedData> {
//            Log.e("Response Status","$responseStatus")
                return htmlDocument {
                    relaxed = true
                    div {
                        withClass = "recordContainer"
                        table {
                            tr {
                                findAll {
                                    map {
                                        ExtractedData(
                                            announcementDate = it.td {
                                                findFirst { text }
                                            },
                                            announcementLink = "https://www.hasil.gov.my" + it.a {
                                                findFirst { attribute("href") }
                                            },
                                            announcement = it.a {
                                                findFirst { text }
                                            }
                                        )

                                    }
                                }
                            }
                        }
                    }
                }
            })
        }
        return extracted
    }
}

fun fetchDetails(urlString: String): List<ExtractedDataDetails> {
    val extracted = skrape(HttpFetcher) {
        request {
            method = Method.GET
            followRedirects = false
            //access cached copy because cant bypass anti crawler protection
            url = urlString
            //url = "https://www.hasil.gov.my/en/announcement/"
            timeout = 15000
            userAgent =
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.97 Safari/537.36"
        }
        response(fun Result.(): List<ExtractedDataDetails> {
 return htmlDocument{
        relaxed = true
        div {
            withClass="recordContainer"
            table {
                    findAll {
                        map {
                            ExtractedDataDetails(
                                announcementTitle = it.strong {
                                    findFirst { text }
                                },
                                announcementDetails = it.br("strong + br"){
                                    findFirst { text }
                                },
                                announcementDetailLinks =
                                mapOf(it.a {
                                    findFirst { attribute("href") }
                                } to
                                        it.a { findFirst { text } },

                                    it.a {
                                        findSecond { attribute("href") }
                                    } to
                                            it.a { findSecond { text } }
                                )

                            )
                        }
                    }

            }
        }
    }
})
    }
    return extracted
}

    //    @Composable
//    fun getData() : List<extractedData>{
//        val extractedData:List<extractedData> by this.ExtractedData.observeAsState(listOf())
//        return extractedData


//    suspend fun getData() : List<extractedData> {
//        return try {
//            fetch()
//        }catch (e: TimeoutError){
//            emptyList()
//        }
//
//    }
//}

//fun fetch(): List<extractedData>{
//    val extracted = skrape(HttpFetcher){
//        request {
//            url = "https://www.hasil.gov.my/en/announcement/"
//            timeout = 10000
//        }
//        response(fun Result.(): List<extractedData> {
//            return htmlDocument{
//                relaxed=true
//                div {
//                    withClass = "recordContainer"
//                    table {
//                        tr{
//                            findAll{
//                                map {
//                                    extractedData(
//                                        announcementDate = it.td{
//                                            findFirst{text}
//                                        },
//                                        announcementLink ="https://www.hasil.gov.my"+ it.a{
//                                            findFirst { attribute("href") }
//                                        },
//                                        announcement = it.a{
//                                            findFirst { text}
//                                        }
//                                    )
//
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        })
//    }
//    return extracted
//}