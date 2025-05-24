package com.sonic.exoplayer

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.Stop
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MusicPlayer()
                }
            }
        }
    }
}

@Composable
fun MusicPlayer() {
    val context = LocalContext.current
    var musicFiles by remember { mutableStateOf(listOf<File>()) }
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            musicFiles = getMusicFiles()
        }
    }

    LaunchedEffect(Unit) {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        val hasPermission = ContextCompat.checkSelfPermission(
            context, permission
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasPermission) {
            permissionLauncher.launch(permission)
        } else {
            musicFiles = getMusicFiles()
        }
    }

    if (musicFiles.isNotEmpty()) {
        SongList(
            musicFiles = musicFiles,
            mediaPlayer = mediaPlayer,
            onMediaPlayerUpdate = { mediaPlayer = it }
        )
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Aplikasi membutuhkan izin untuk mengakses file MP3.")
        }
    }
}

@Composable
fun SongList(
    musicFiles: List<File>,
    mediaPlayer: MediaPlayer?,
    onMediaPlayerUpdate: (MediaPlayer?) -> Unit
) {
    val context = LocalContext.current
    var currentIndex by remember { mutableStateOf(-1) }
    var isPlaying by remember { mutableStateOf(false) }

    fun playSongAtIndex(index: Int) {
        if (index in musicFiles.indices) {
            mediaPlayer?.release()
            val newPlayer = MediaPlayer()
            newPlayer.setDataSource(musicFiles[index].absolutePath)
            newPlayer.prepare()
            newPlayer.start()
            onMediaPlayerUpdate(newPlayer)
            isPlaying = true
            currentIndex = index
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Daftar Lagu", style = MaterialTheme.typography.h6)

        LazyColumn {
            itemsIndexed(musicFiles) { index, file ->
                Card(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .fillMaxWidth()
                        .clickable { playSongAtIndex(index) },
                    elevation = 4.dp
                ) {
                    Row(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = file.name,
                            modifier = Modifier.weight(1f),
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }

        if (currentIndex >= 0) {
            val currentFile = musicFiles[currentIndex]
            Text(
                text = "Sedang memutar: ${currentFile.name}",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(top = 8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(onClick = {
                    if (currentIndex > 0) {
                        playSongAtIndex(currentIndex - 1)
                    }
                }) {
                    Icon(imageVector = Icons.Default.SkipPrevious, contentDescription = "Previous")
                }

                IconButton(onClick = {
                    if (isPlaying) {
                        mediaPlayer?.pause()
                    } else {
                        mediaPlayer?.start()
                    }
                    isPlaying = !isPlaying
                }) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play"
                    )
                }

                IconButton(onClick = {
                    mediaPlayer?.stop()
                    mediaPlayer?.release()
                    onMediaPlayerUpdate(null)
                    currentIndex = -1
                    isPlaying = false
                }) {
                    Icon(imageVector = Icons.Default.Stop, contentDescription = "Stop")
                }

                IconButton(onClick = {
                    if (currentIndex + 1 < musicFiles.size) {
                        playSongAtIndex(currentIndex + 1)
                    }
                }) {
                    Icon(imageVector = Icons.Default.SkipNext, contentDescription = "Next")
                }
            }
        }
    }
}

fun getMusicFiles(): List<File> {
    val musicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
    return musicDir?.listFiles()?.filter { it.extension.equals("mp3", ignoreCase = true) } ?: emptyList()
}
