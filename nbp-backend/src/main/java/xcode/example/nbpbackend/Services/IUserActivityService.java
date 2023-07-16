package xcode.example.nbpbackend.Services;

import xcode.example.nbpbackend.models.CurrentRequest;
import xcode.example.nbpbackend.models.CurrentResponse;
import xcode.example.nbpbackend.models.UserActivityDTO;

import java.util.List;

public interface IUserActivityService {

    CurrentResponse getCurrent(CurrentRequest currentRequest);
    List<UserActivityDTO> getUsersActivity();
}
