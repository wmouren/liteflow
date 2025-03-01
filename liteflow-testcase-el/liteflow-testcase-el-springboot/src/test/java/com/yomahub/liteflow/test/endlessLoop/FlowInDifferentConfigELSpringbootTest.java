package com.yomahub.liteflow.test.endlessLoop;

import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.exception.CyclicDependencyException;
import com.yomahub.liteflow.property.LiteflowConfig;
import com.yomahub.liteflow.property.LiteflowConfigGetter;
import com.yomahub.liteflow.test.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.Resource;

/**
 * 测试多文件情况下 chain 死循环逻辑
 *
 * @author luo yi
 * @since 2.11.1
 */
@SpringBootTest(classes = FlowInDifferentConfigELSpringbootTest.class)
@EnableAutoConfiguration
@ComponentScan({ "com.yomahub.liteflow.test.endlessLoop.cmp"})
public class FlowInDifferentConfigELSpringbootTest extends BaseTest {

	@Resource
	private FlowExecutor flowExecutor;

	// 测试 chain 死循环
	@Test
	public void testChainEndlessLoop() {
		Assertions.assertThrows(CyclicDependencyException.class, () -> {
			LiteflowConfig config = LiteflowConfigGetter.get();
			config.setRuleSource("endlessLoop/flow-sub1.el.xml,endlessLoop/flow-sub2.el.yml");
			config.setSupportMultipleType(true);
			flowExecutor.reloadRule();
		});
	}

}
