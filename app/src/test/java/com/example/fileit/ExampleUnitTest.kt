package com.example.fileit

import com.example.fileit.webcrawler.ExtractedData
import com.example.fileit.webcrawler.ExtractedDataDetails
import it.skrape.core.htmlDocument
import it.skrape.fetcher.*
import it.skrape.selects.html5.*
import org.junit.Test

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
        val stringArr = arrayOf("https://www.hasil.gov.my/en/announcement-detail/?recordId=01721dfc-6081-4fd9-9a4d-a7074302ce76",
            "https://www.hasil.gov.my/en/announcement-detail/?recordId=4bc6237a-9363-4c2c-bb62-695458c6dbb4",
            "https://www.hasil.gov.my/en/announcement-detail/?recordId=aa04622f-2cbd-4edc-a5d5-1ed7b3187f40")
        //println(fetchDetails(stringArr[0]))
        println(fetchDetails(stringArr[0]))
        //println(fetchDetails(stringArr[1]))
        //println(fetchDetails(stringArr[2]))
    }


//    fun fetch(): List<ExtractedData>{
//        val extracted = skrape(HttpFetcher){
//            request {
//                url = "http://webcache.googleusercontent.com/search?q=cache:https://www.hasil.gov.my/en/announcement/"
//            }
//            response(fun Result.(): List<ExtractedData> {
//                println(responseStatus)
//             return htmlDocument{
//                 relaxed=true
//
//                    div {
//                        withClass = "recordContainer"
//                        table {
//                            tr{
//                                findAll{
//                                    map {
//                                        ExtractedData(
//                                            announcementDate = it.td{
//                                                findFirst{text}
//                                            },
//                                            announcementLink ="https://www.hasil.gov.my"+ it.a{
//                                                findFirst { attribute("href") }
//                                            },
//                                            announcement = it.a{
//                                                findFirst { text}
//                                            }
//                                        )
//
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//})
//        }
//        return extracted
//    }
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
            println(responseStatus)
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
                                        announcementDetails =
                                            findFirst(cssSelector = "td") {text},
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





}