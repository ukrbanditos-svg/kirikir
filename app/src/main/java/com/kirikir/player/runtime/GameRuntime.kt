package com.kirikir.player.runtime
import com.kirikir.player.games.GameInfo
interface GameRuntime{fun supports(game:GameInfo):Boolean;fun launch(game:GameInfo):Result<Unit>}