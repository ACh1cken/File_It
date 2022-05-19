package com.example.fileit.webcrawler

import androidx.lifecycle.ViewModel
import it.skrape.core.htmlDocument
import it.skrape.fetcher.HttpFetcher
import it.skrape.fetcher.Result
import it.skrape.fetcher.response
import it.skrape.fetcher.skrape
import it.skrape.selects.html5.*

data class extractedData(
    var announcement: String,
    var announcementLink: String,
    var announcementDate: String,

    )

class webcrawler : ViewModel(){
//    private var _extractedData: MutableLiveData<List<extractedData>> = MutableLiveData(emptyList())
//    val ExtractedData: LiveData<List<extractedData>> = _extractedData

//    fun updateData() {
//        viewModelScope.launch {
//            _extractedData.postValue(fetch())
//        }
//    }
//
//    fun flush() {
//        _extractedData.postValue(emptyList())
//    }
//    @Composable
//    fun getData() : List<extractedData>{
//        val extractedData:List<extractedData> by this.ExtractedData.observeAsState(listOf())
//        return extractedData
//    }

    fun getData() : List<extractedData> {
        return fetch()
    }
}

fun fetch(): List<extractedData>{
    val extracted = skrape(HttpFetcher){
        request {
            url = "https://www.hasil.gov.my/en/announcement/"
        }
        response(fun Result.(): List<extractedData> {
            return htmlDocument{
                //relaxed=true
                div {
                    withClass = "recordContainer"
                    table {
                        tr{
                            findAll{
                                map {
                                    extractedData(
                                        announcementDate = it.td{
                                            findFirst{text}
                                        },
                                        announcementLink ="https://www.hasil.gov.my"+ it.a{
                                            findFirst { attribute("href") }
                                        },
                                        announcement = it.a{
                                            findFirst { text}
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


//suspend fun fetch(): List<extractedData> = withContext(Dispatchers.IO){
//    skrape(HttpFetcher){
//        request {
//            url = "https://www.hasil.gov.my/en/announcement/"
//        }
//        response(fun Result.(): List<extractedData> {
//
//return htmlDocument{
//        div {
//            withClass = "recordContainer"
//            table {
//                tr{
//                    findAll{
//                        map {
//                            extractedData(
//                                announcementDate = it.td{
//                                    findFirst{text}
//                                },
//                                announcementLink = "https://www.hasil.gov.my"+ it.a{
//                                    findFirst { attribute("href") }
//                                },
//                                announcement = it.a{
//                                    findFirst { text }
//                                }
//                            )
//
//                        }
//                    }
//                }
//            }
//        }
//    }
//})
//    }
//}