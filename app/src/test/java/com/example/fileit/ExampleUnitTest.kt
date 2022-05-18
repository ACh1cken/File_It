package com.example.fileit

import android.provider.Browser
import com.example.fileit.webcrawler.extractedData
import it.skrape.core.htmlDocument
import it.skrape.fetcher.*
import it.skrape.matchers.toBe
import it.skrape.matchers.toBePresentExactlyOnce
import it.skrape.selects.html5.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

//    @Test
//    fun addition_isCorrect() {
//        assertEquals(4, 2 + 2)
//    }

//    @Test
//    fun crawlertest(){
//        skrape(BrowserFetcher){
//            request {
//                url = "https://www.hasil.gov.my/en/announcement/"
//            }
//            response(fun Result.(): String? {
//                 return htmlDocument{
//                        relaxed = true
//                        div {
//                            withClass = "recordContainer"
//                            table {
//                                tr{
//                                    findAll{
//                                        td{
//                                           findFirst{text toBe  "29 Apr 2022"}
//
//                                        }
//                                        a{
//                                            findFirst { attribute("href") toBe "/en/announcement-detail/?recordId=14cf41e1-0078-48e5-b02b-f21613bf70c0" }
//                                        }
//                                        a{
//                                            findFirst { text toBe "Latest IRBM Tax Audit Framework"}
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                })
//        }
//    }

    @Test
    fun getdata(){
        println(fetch())
    }


    fun fetch(): List<extractedData>{
        val extracted = skrape(HttpFetcher){
            request {
                url = "https://www.hasil.gov.my/en/announcement/"
            }
            response(fun Result.(): List<extractedData> {
             return htmlDocument{
                 relaxed=true
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

}