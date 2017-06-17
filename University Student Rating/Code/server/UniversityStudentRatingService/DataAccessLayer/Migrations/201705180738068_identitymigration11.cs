namespace DataAccessLayer.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class identitymigration11 : DbMigration
    {
        public override void Up()
        {
            DropForeignKey("dbo.AspNetUsers", "UserProfileId", "dbo.UserProfile");
            DropIndex("dbo.AspNetUsers", new[] { "UserProfileId" });
            AlterColumn("dbo.AspNetUsers", "UserProfileId", c => c.Int());
            CreateIndex("dbo.AspNetUsers", "UserProfileId");
            AddForeignKey("dbo.AspNetUsers", "UserProfileId", "dbo.UserProfile", "Id");
        }
        
        public override void Down()
        {
            DropForeignKey("dbo.AspNetUsers", "UserProfileId", "dbo.UserProfile");
            DropIndex("dbo.AspNetUsers", new[] { "UserProfileId" });
            AlterColumn("dbo.AspNetUsers", "UserProfileId", c => c.Int(nullable: false));
            CreateIndex("dbo.AspNetUsers", "UserProfileId");
            AddForeignKey("dbo.AspNetUsers", "UserProfileId", "dbo.UserProfile", "Id", cascadeDelete: true);
        }
    }
}
