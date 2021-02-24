package com.misakikawaguchi.implicitintentsample

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import java.net.URLEncoder

class MainActivity : AppCompatActivity() {

    // 緯度フィールド
    private var _latitude = 0.0
    // 経度フィールド
    private var _longitude = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // GPS機能を有効にする
        // LocationManagerオブジェクトを取得
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // 位置情報が更新された際のリスなオブジェクトを生成
        val locationListener = GPSLocationListener()
        // 位置情報の追跡を開始
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
    }

    // 地図検索ボタンタップ時の処理
    fun onMapSearchButtonClick(view: View) {
        // 入力欄に入力されたキーワードを取得
        val tvSearchWord = findViewById<EditText>(R.id.tvSearchWord)
        var searchWord = tvSearchWord.text.toString()
        // 入力されたキーワードをURIエンコード
        searchWord = URLEncoder.encode(searchWord, "UTF-8")

        // マップアプリと連携するURI文字列を生成
        val uriStr = "geo:0.0?q=${searchWord}"
        // URI文字列からURIオブジェクトを生成
        val uri = Uri.parse(uriStr)

        // Intentオブジェクトを生成
        val intent = Intent(Intent.ACTION_VIEW, uri)
        // アクティビティを起動
        startActivity(intent)
    }

    // 地図表示ボタンタップ時の処理
    fun onMapShowCurrentButtonClick(view: View) {
        // フィールドの緯度と経度の値を元にマップアプリと連携するURI文字列を生成
        val uriStr = "geo:${_latitude},${_longitude}"
        // URI文字列からURIオブジェクトを生成
        val uri = Uri.parse(uriStr)

        // Intentオブジェクトを生成
        val intent = Intent(Intent.ACTION_VIEW, uri)
        // アクティビティを起動
        startActivity(intent)
    }
}