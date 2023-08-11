package com.example.testandroid.utils

import android.annotation.TargetApi
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatActivity
import android.util.DisplayMetrics
import android.util.Log
import java.util.*


@Suppress("DEPRECATION")
class LanguageSettings {
    companion object {
        fun getSystemLocaleLegacy(config: Configuration): Locale? {
            return config.locale
        }

        @TargetApi(Build.VERSION_CODES.N)
        fun getSystemLocale(config: Configuration): Locale? {
            return config.getLocales().get(0)
        }

        fun setSystemLocaleLegacy(config: Configuration, locale: Locale?) {
            config.locale = locale
        }

        @TargetApi(Build.VERSION_CODES.N)
        fun setSystemLocale(config: Configuration, locale: Locale?) {
            config.setLocale(locale)
        }

        fun getLanguage(): String {
//            if (Paper.book().contains(Const.APP_LANGUAGE)) {
//                return Paper.book().read(Const.APP_LANGUAGE)
//            }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Log.d("CURRENT_LOCAl", LocaleList.getDefault().get(0).language)
                    return LocaleList.getDefault().get(0).language


                } else {
                    Log.d("CURRENT_LOCAl", Locale.getDefault().language)
                    return Locale.getDefault().language;
                }
            }


            fun setLanguage(context: AppCompatActivity, languageCode: String?) {
                val res: Resources = context.resources
                val configuration: Configuration = res.configuration
                val newConfig = Configuration()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                    val res = context.resources
                    val dm: DisplayMetrics = res.displayMetrics
                    val conf = res.configuration
                    conf.setLocale(
                        Locale(languageCode)
                    )
                    res.updateConfiguration(conf, dm);

                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val locale = Locale(languageCode)
                    Locale.setDefault(locale)
                    val config =
                        context.resources.configuration
                    config.setLocale(locale)
                    context.createConfigurationContext(configuration)
                    context.resources
                        .updateConfiguration(configuration, context.resources.displayMetrics)
                } else {
                    if (languageCode == "fr") configuration.locale = Locale.FRENCH
                    if (languageCode == "en") configuration.locale = Locale.ENGLISH

                }

                context.onConfigurationChanged(configuration)
            }

            fun changeLang(context: Context, lang_code: String): ContextWrapper? {
                var context: Context = context
                val sysLocale: Locale
                val rs: Resources = context.resources
                val config = rs.configuration
                sysLocale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    config.locales[0]
                } else {
                    config.locale
                }
                if (lang_code != "" && sysLocale.language != lang_code) {
                    val locale = Locale(lang_code)
                    Locale.setDefault(locale)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        config.setLocale(locale)
                    } else {
                        config.locale = locale
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        context = context.createConfigurationContext(config)
                    } else {
                        context.resources
                            .updateConfiguration(config, context.resources.displayMetrics)
                    }
                }
                return ContextWrapper(context)
            }


        }
    }