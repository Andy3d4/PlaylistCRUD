package eu.itcrafter.playlist.controller.song;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/song")
public class SongController {

    private final SongService songService;
}
