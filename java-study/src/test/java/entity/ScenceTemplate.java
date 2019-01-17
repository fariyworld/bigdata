package entity;

import java.util.List;

public class ScenceTemplate {
	//场景模板名称
	private String scenceTempName;
	//触发后发送的topic名称
	private String topic="scenetopic";
	//创建模板(数据实体创立条件)
	private List<ConditionInfo> createConList;
	//消除模板(数据实体消除条件)
	private List<ConditionInfo> breakConList;
	//地域信息模板
	private ScenceDataTemplate areaTemplate;
	//黑名单是否黑开启,默认为true
	private boolean blacklist=true;
	//白名单是否开启,默认为true
	private boolean whitelist=true;

}
