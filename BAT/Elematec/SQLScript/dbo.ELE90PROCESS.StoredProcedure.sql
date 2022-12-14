USE [ELEMATEC]
GO
/****** Object:  StoredProcedure [dbo].[ELE90PROCESS]    Script Date: 2022/02/17 14:35:42 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- ********************************************************************************************
-- システム名 : 入出庫管理システム
-- プログラムID : ELE90PROCESS
-- 処理名    : 番号発番処理
-- 機能概要  : 
-- ********************************************************************************************
CREATE PROCEDURE [dbo].[ELE90PROCESS]
(
	@NumberKubun		varchar(10)
	,@Year				int
	,@Month				int
	,@MaxNumber			int
	,@OperatorId		varchar(20)
	,@Num				int OUTPUT
)
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

	SELECT
		@Num = NUMBER
	FROM
		M_NUMBERINCREMENT
	WHERE
		NUMBER_KUBUN = @NumberKubun
	AND
		YEAR = @Year
	AND
		MONTH = @Month

	if @Num is null
		BEGIN
			INSERT INTO [dbo].[M_NUMBERINCREMENT]
					   ([NUMBER_KUBUN]
					   ,[YEAR]
					   ,[MONTH]
					   ,[NUMBER]
					   ,[MAX_NUMBER]
					   ,[REGIST_DATE]
					   ,[REGIST_NAME]
					   ,[UPDATE_DATE]
					   ,[UPDATE_NAME])
				 VALUES
					   (@NumberKubun
					   ,@Year
					   ,@Month
					   ,0
					   ,@MaxNumber
					   ,FORMAT(SYSDATETIME(), 'yyyyMMddHHmmss')
					   ,@OperatorId
					   ,FORMAT(SYSDATETIME(), 'yyyyMMddHHmmss')
					   ,@OperatorId
					   )
			SET @Num = 0;
		END

	--	番号をインクリメント
	set @Num += 1

	--	インクリメントした番号を番号マスタに反映
	UPDATE [dbo].[M_NUMBERINCREMENT]
	   SET [NUMBER] = @Num
		  ,[UPDATE_DATE] = FORMAT(SYSDATETIME(), 'yyyyMMddHHmmss')
		  ,[UPDATE_NAME] = @OperatorId
	WHERE
		NUMBER_KUBUN = @NumberKubun
	AND
		YEAR = @Year
	AND
		MONTH = @Month

	--	インクリメントした番号戻す
--	SELECT @Num

END
GO
