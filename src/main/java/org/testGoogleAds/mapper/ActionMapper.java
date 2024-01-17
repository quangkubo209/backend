package org.testGoogleAds.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.testGoogleAds.model.Action;

import java.util.List;

@Mapper
public interface ActionMapper {

    @Select("SELECT * FROM action")
    List<Action> getAllActions();


    @Insert("INSERT INTO action ( fileName, status, detail) VALUES ( #{fileName}, #{status}, #{detail})")
    void insertAction(Action action);

}
