package com.davgeoand.api.model.list;

import com.davgeoand.api.model.user.UserStepConnection;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserStepConnectionList extends ArrayList<UserStepConnection> {
}
