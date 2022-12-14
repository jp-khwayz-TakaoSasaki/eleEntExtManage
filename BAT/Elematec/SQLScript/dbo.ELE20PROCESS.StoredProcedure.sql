USE [ELEMATEC]
GO
/****** Object:  StoredProcedure [dbo].[ELE20PROCESS]    Script Date: 2022/02/17 14:35:42 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- ********************************************************************************************
-- システム名 : 入出庫管理システム
-- プログラムID : ELE20PROCESS
-- 処理名    : 無償品情報取込
-- 機能概要  : 
-- ********************************************************************************************
CREATE PROCEDURE [dbo].[ELE20PROCESS] 
	(
		 @ImportFilePath		nvarchar(1000)
		,@ErrorFilePath		nvarchar(1000)
		,@ErrorNumber		int OUTPUT
		,@ErrorMessage		nvarchar(4000) OUTPUT
	)
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

	--	変数
	DECLARE	@sql		nvarchar(1000);
	DECLARE @rtnCode	int;

	SET @rtnCode = 0;
	SET @ErrorNumber = 0;
	SET @ErrorMessage = N'';

	--	ワークテーブルの削除
	TRUNCATE TABLE W_MUSHOUHIN_TEMP

	--	BULK INSERTの生成
	SET @sql = N'BULK INSERT W_MUSHOUHIN_TEMP ' +
				N'FROM ''' + @ImportFilePath + ''' ' +
				N'WITH ( ' +
				N'FORMAT = ''CSV'' ' +
				N', FIELDTERMINATOR = ''\t'' ' +
				N', FIRSTROW = 2 ' +
				N', DATAFILETYPE=''char'' ' +
				N', CODEPAGE = ''65001'' ' +
				N', ERRORFILE = ''' + @ErrorFilePath + ''' ' +
				N') ';

	BEGIN TRY
		EXEC sp_executesql @sql;
		SET @rtnCode = 1;
	END TRY

	BEGIN CATCH
		SET @ErrorNumber = ERROR_NUMBER();
		SET @ErrorMessage = ERROR_MESSAGE();
		SET @rtnCode = 9;
	END CATCH

	return @rtnCode;
END
GO
