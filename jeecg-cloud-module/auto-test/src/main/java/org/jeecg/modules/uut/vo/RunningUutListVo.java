package org.jeecg.modules.uut.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import org.jeecg.modules.uut.entity.RunningUutListHistory;
import org.jeecg.modules.uut.entity.RunningUutManager;

import java.io.Serializable;
import java.util.List;

@Data
/**
 * @Author: test
 * */
public class RunningUutListVo implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
    private String id;
    /**被测对象创建方式*/
    private String uutChooseType;
	/**被测对象名称*/
    private String uutName;
	/**被测对象类型*/
    private String uutType;
	/**被测对象标识*/
    private String uutCode;
	/**被测对象版本*/
    private String uutVersion;
	/**选择资产库*/
    private String uutAssetsId;
	/**资产库详情*/
    private String uutAssetsDetail;
	/**被测件*/
    private String uutFile;
	/**创建人*/
    private String createBy;
	/**创建日期*/
    private java.util.Date createTime;
	/**更新人*/
    private String updateBy;
	/**更新日期*/
    private java.util.Date updateTime;
	/**所属部门*/
    private String sysOrgCode;
	/**状态*/
    private String uutStatus;
	/**分析状态*/
    private String analyzeStatus;
	/**测试模板*/
    private String testTemplate;
	/**需求规格说明文档*/
    private String uutStandardFile;
	/**测试依据*/
    private String uutBasisFile;
	/**测试说明*/
    private String uutExplainFile;
	/**历史测试用例设计单*/
    private String uutHteFile;
	/**执行单*/
    private String uutExecuteFile;
	/**报告单*/
    private String uutReportFile;
	/**回归用例*/
    private String uutReFile;
	/**其他*/
    private String uutOtherFile;
    /**版本状态*/
    private String versionStatus;
    /** 逻辑删除标识*/
    private Integer deleteFlag;
    /** 当前用户code*/
    private String applier;
    /** 当前用户真实姓名*/
    private String applierDictText;
    /** 流程发起时间*/
    private String startDate;
    @TableField(exist = false)
    /**编辑操作用到这个节点，保存修改过的字段名称和字段值*/
    private List<RunningUutListHistory> runningUutListHistory;

    private List<RunningUutManager> runningManager;
}
