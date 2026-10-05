package com.kirikir.player

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.kirikir.player.databinding.ActivityMainBinding
import com.kirikir.player.games.*
import com.kirikir.player.runtime.GameStager
import com.kirikir.player.runtime.NativeKirikiriBridge
import kotlin.concurrent.thread

class MainActivity:AppCompatActivity(){
 private lateinit var binding:ActivityMainBinding
 private lateinit var store:GameStore
 private lateinit var scanner:GameScanner
 private lateinit var stager:GameStager
 private var currentGame:GameInfo?=null
 private val chooseFolder=registerForActivityResult(ActivityResultContracts.OpenDocumentTree()){u->if(u!=null)importGame(u)}
 override fun onCreate(s:Bundle?){super.onCreate(s);binding=ActivityMainBinding.inflate(layoutInflater);setContentView(binding.root);store=GameStore(this);scanner=GameScanner(this);stager=GameStager(this);binding.addGameButton.setOnClickListener{chooseFolder.launch(null)};binding.launchGameButton.setOnClickListener{stageAndProbe()};render(store.load())}
 private fun importGame(u:Uri){try{contentResolver.takePersistableUriPermission(u,Intent.FLAG_GRANT_READ_URI_PERMISSION)}catch(_:SecurityException){};val g=scanner.scan(u);if(g==null){binding.statusText.text="В выбранной папке не найдено файлов .xp3";return};store.save(g);render(g)}
 private fun stageAndProbe(){val g=currentGame?:return;binding.launchGameButton.isEnabled=false;binding.statusText.text="Подготовка файлов игры…";thread{val result=stager.stage(g){name->runOnUiThread{binding.statusText.text="Копирование: $name"}};runOnUiThread{result.onSuccess{gameDir->val probe=if(NativeKirikiriBridge.isAvailable()) NativeKirikiriBridge.nativeProbeGame(gameDir.absolutePath) else -2;binding.statusText.text=when(probe){1->"Игра подготовлена ✓\n"+gameDir.absolutePath+"\n\nNative runtime видит XP3. Следующий шаг — запуск полного KiriKiri core.";0->"Runtime запущен, но XP3 не найден.";else->"Не удалось инициализировать native runtime (код $probe)."}}.onFailure{e->binding.statusText.text="Ошибка подготовки: "+e.message};binding.launchGameButton.isEnabled=true}}}
 private fun render(g:GameInfo?){currentGame=g;binding.launchGameButton.isEnabled=g!=null;binding.statusText.text=if(g==null)getString(R.string.empty_library) else buildString{appendLine(g.name);appendLine();appendLine("KiriKiri обнаружен ✓");appendLine("XP3: "+g.xp3Files.size);g.xp3Files.forEach{appendLine("• "+it)};appendLine();append("Нажми «Запустить» для подготовки runtime.")}}
}
