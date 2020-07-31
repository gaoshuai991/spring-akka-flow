package cn.gss.flow.core.util;

import cn.gss.flow.core.exception.JsonGenericException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

import java.io.IOException;

/**
 * Created by JackieGao on 2020/7/29.
 */
@UtilityClass
public class ObjectMapperUtils {
  private final ObjectMapper MAPPER = new ObjectMapper();

  public ObjectMapper getMapper() {
    return MAPPER;
  }

  public String asString(Object value) {
    try {
      return MAPPER.writeValueAsString(value);
    } catch (JsonProcessingException e) {
      throw new JsonGenericException(e);
    }
  }

  public <T> T asValue(String value, Class<T> valueType) {
    try {
      return MAPPER.readValue(value, valueType);
    } catch (IOException e) {
      throw new JsonGenericException(e);
    }
  }

  public <T> T asValue(String value, TypeReference<T> valueTypeRef) {
    try {
      return MAPPER.readValue(value, valueTypeRef);
    } catch (IOException e) {
      throw new JsonGenericException(e);
    }
  }

  public <T> T toValue(Object fromValue, Class<T> toValueType) {
    try {
      return MAPPER.convertValue(fromValue, toValueType);
    } catch (IllegalArgumentException e) {
      throw new JsonGenericException(e);
    }
  }

  public <T> T toValue(Object fromValue, TypeReference<T> toValueTypeRef) {
    try {
      return MAPPER.convertValue(fromValue, toValueTypeRef);
    } catch (IllegalArgumentException e) {
      throw new JsonGenericException(e);
    }
  }

  public <T> T fromTree(TreeNode tree, Class<T> toValueType) {
    try {
      return MAPPER.treeToValue(tree, toValueType);
    } catch (IllegalArgumentException | JsonProcessingException e) {
      throw new JsonGenericException(e);
    }
  }

  public <T> T fromTree(
      TreeNode tree, TypeReference<T> toValueTypeRef
  ) throws JsonProcessingException {
    try {
      return MAPPER.readValue(
          MAPPER.treeAsTokens(tree), MAPPER.getTypeFactory().constructType(toValueTypeRef));
    } catch (IllegalArgumentException | IOException e) {
      throw new JsonGenericException(e);
    }
  }

  public JsonNode toTree(Object fromValue) {
    try {
      return MAPPER.valueToTree(fromValue);
    } catch (IllegalArgumentException e) {
      throw new JsonGenericException(e);
    }
  }
}
