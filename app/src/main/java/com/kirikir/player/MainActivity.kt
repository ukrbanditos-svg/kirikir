package com.kirikir.player
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.kirikir.player.databinding.ActivityMainBinding
import com.kirikir.player.games.*
class MainActivity:AppCompatActivity(){private lateinit var binding:ActivityMainBinding;private lateinit var store:GameStore;private lateinit var scanner:GameScanner;private val chooseFolder=registerForActivityResult(ActivityResultContracts.OpenDocumentTree()){u->if(u!=null)importGame(u)};override fun onCreate(s:Bundle?){super.onCreate(s);binding=ActivityMainBinding.inflate(layoutInflater);setContentView(binding.root);store=GameStore(this);scanner=GameScanner(this);binding.addGameButton.setOnClickListener{chooseFolder.launch(null)};render(store.load())};private fun importGame(u:Uri){try{contentResolver.takePersistableUriPermission(u,Intent.FLAG_GRANT_READ_URI_PERMISSION)}catch(_:SecurityException){};val g=scanner.scan(u);if(g==null){binding.statusText.text="В выбранной папке не найдено файлов .xp3";return};store.save(g);render(g)};private fun render(g:GameInfo?){binding.statusText.text=if(g==null)getString(R.string.empty_library) else buildString{appendLine(g.name);appendLine();appendLine("KiriKiri обнаружен ✓");appendLine("XP3: "+g.xp3Files.size);g.xp3Files.forEach{appendLine("• "+it)};appendLine();append("Следующий этап: подключение runtime.")}}}