package com.searchandroidlist.utils

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
class PermissionUtils {

    companion object {
        const val CAMERA_REQUEST_CODE = 1001
        const val STORAGE_REQUEST_CODE = 1002

        fun checkCameraPermission(appCompatActivity: AppCompatActivity): Boolean {
            val permission = ContextCompat.checkSelfPermission(
                appCompatActivity,
                Manifest.permission.CAMERA
            )
            if (permission != PackageManager.PERMISSION_GRANTED) {
                return false
            }
            return true
        }

        fun requestCameraPermission(appCompatActivity: AppCompatActivity) {
            ActivityCompat.requestPermissions(
                appCompatActivity,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_REQUEST_CODE
            )
        }

        fun requestCameraPermissionDenied(appCompatActivity: AppCompatActivity): Boolean {
            return ActivityCompat.shouldShowRequestPermissionRationale(
                appCompatActivity,
                Manifest.permission.CAMERA
            )
        }

        fun checkStoragePermission(appCompatActivity: AppCompatActivity): Boolean {
            val permission = ContextCompat.checkSelfPermission(
                appCompatActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            val permission1 = ContextCompat.checkSelfPermission(
                appCompatActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            if (permission != PackageManager.PERMISSION_GRANTED && permission1 != PackageManager.PERMISSION_GRANTED) {
                return false
            }
            return true
        }

        fun requestStoragePermission(appCompatActivity: AppCompatActivity) {
            ActivityCompat.requestPermissions(
                appCompatActivity,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                STORAGE_REQUEST_CODE
            )
        }

        fun requestStoragePermissionDenied(appCompatActivity: AppCompatActivity): Boolean {
            return ActivityCompat.shouldShowRequestPermissionRationale(
                appCompatActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) && ActivityCompat.shouldShowRequestPermissionRationale(
                appCompatActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
    }
}