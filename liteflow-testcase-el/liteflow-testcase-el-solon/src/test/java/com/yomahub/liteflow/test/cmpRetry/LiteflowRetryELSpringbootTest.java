package com.yomahub.liteflow.test.cmpRetry;

import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.flow.LiteflowResponse;
import com.yomahub.liteflow.test.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.noear.snack.ONode;
import org.noear.solon.annotation.Inject;
import org.noear.solon.test.SolonJUnit5Extension;
import org.noear.solon.test.annotation.TestPropertySource;

/**
 * 测试springboot下的节点执行器
 *
 * @author Bryan.Zhang
 * @since 2.5.10
 */
@ExtendWith(SolonJUnit5Extension.class)
@TestPropertySource("classpath:/cmpRetry/application.properties")
public class LiteflowRetryELSpringbootTest extends BaseTest {

	@Inject
	private FlowExecutor flowExecutor;

	// 全局重试配置测试
	@Test
	public void testRetry1() {
		LiteflowResponse response = flowExecutor.execute2Resp("chain1", "arg");
		Assertions.assertTrue(response.isSuccess());
		Assertions.assertEquals("a==>b==>b==>b", response.getExecuteStepStr());
	}

	// 单个组件重试配置测试
	@Test
	public void testRetry2() {
		LiteflowResponse response = flowExecutor.execute2Resp("chain2", "arg");
		Assertions.assertFalse(response.isSuccess());
		Assertions.assertEquals("c==>c==>c==>c==>c==>c", response.getExecuteStepStr());
	}

	// 单个组件指定异常，但抛出的并不是指定异常的场景测试
	@Test
	public void testRetry3() {
		LiteflowResponse response = flowExecutor.execute2Resp("chain3", "arg");
		Assertions.assertFalse(response.isSuccess());
	}

	// 单个组件指定异常重试，抛出的是指定异常或者
	@Test
	public void testRetry4() {
		LiteflowResponse response = flowExecutor.execute2Resp("chain4", "arg");
		Assertions.assertFalse(response.isSuccess());
		Assertions.assertEquals("e==>e==>e==>e==>e==>e", response.getExecuteStepStr());
	}

}
