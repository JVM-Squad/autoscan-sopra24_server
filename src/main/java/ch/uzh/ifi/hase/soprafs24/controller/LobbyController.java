package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.LobbyIdDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.LobbyStateDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserTokenDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class LobbyController {

    private final LobbyService lobbyService;

    public LobbyController(LobbyService lobbyService) {
        this.lobbyService = lobbyService;
    }

    @PostMapping("/lobbies")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public LobbyIdDTO createLobby(@RequestBody UserTokenDTO userTokenDTO) {
        User host = DTOMapper.INSTANCE.convertUserTokenDTOtoEntity(userTokenDTO);
        Lobby createdLobby = lobbyService.createLobby(host);
        return DTOMapper.INSTANCE.convertEntityToLobbyIdDTO(createdLobby);
    }

    @DeleteMapping("/lobbies/{lobbyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLobby(@PathVariable String lobbyId, UserTokenDTO userTokenDTO) {
        User userToken = DTOMapper.INSTANCE.convertUserTokenDTOtoEntity(userTokenDTO);
        lobbyService.deleteLobby(lobbyId, userToken);
    }
}
