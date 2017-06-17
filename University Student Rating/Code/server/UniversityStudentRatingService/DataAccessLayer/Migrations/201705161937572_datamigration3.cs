namespace DataAccessLayer.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class datamigration3 : DbMigration
    {
        public override void Up()
        {
            DropForeignKey("dbo.University", "Country_Id", "dbo.Country");
            DropIndex("dbo.University", new[] { "Country_Id" });
        }
        
        public override void Down()
        {
            AddColumn("dbo.University", "Country_Id", c => c.Int());
            CreateIndex("dbo.University", "Country_Id");
            AddForeignKey("dbo.University", "Country_Id", "dbo.Country", "Id");
        }
    }
}
