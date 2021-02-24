package com.misakikawaguchi.implicitintentsample

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.app.ActivityCompat
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

        // ACCESS_FINE_LOCATIONの許可が降りていない場合
        if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // ACCESS_FINE_LOCATIONの許可を求めるダイアログを表示。その際リクエストコードを1000に設定
            val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
            ActivityCompat.requestPermissions(this@MainActivity, permissions, 1000)
            // onCreate()メソッドを終了
            return
        }
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

    // ロケーションリスナクラス
    private inner class GPSLocationListener : LocationListener {
        override fun onLocationChanged(location: Location) {
            // 引数のLocationオブジェクトから緯度を取得
            _latitude = location.latitude
            // 引数びLocationオブジェクトから経度を取得
            _longitude = location.longitude

            // 取得した緯度をTextViewに表示
            val tvLatitude = findViewById<TextView>(R.id.tvLatitude)
            tvLatitude.text = _latitude.toString()
            // 取得した経度をTextViewに表示
            val tvLongitude = findViewById<TextView>(R.id.tvLongitude)
            tvLongitude.text = _longitude.toString()
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle?) {}

        override fun onProviderEnabled(provider: String) {}

        override fun onProviderDisabled(provider: String) {}
    }
}